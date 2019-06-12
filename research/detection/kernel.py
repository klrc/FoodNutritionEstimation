import random
import numpy as np

from . import configs
from .FoodMask60 import FoodMask60
from .mrcnn import utils, visualize
from .mrcnn import model as modellib
from .mrcnn.model import log

# # %matplotlib inline
# inter_num = 0
# np.set_printoptions(threshold=np.inf)


class Kernel():
    # Directory to cached data
    CACHE_DIR = 'data/__cache__/detection'
    # Preprocessed training data here
    FEEDER_DIR = f'data/FoodMask60/build'
    # Directory to save logs and trained model
    LOGS_DIR = f'{CACHE_DIR}/logs'
    # Local path to trained weights file
    COCO_MODEL_PATH = 'data/mask_rcnn_coco.h5'
    # Download COCO trained weights from Releases if needed
    # if not os.path.exists(COCO_MODEL_PATH):
    #     utils.download_trained_weights(COCO_MODEL_PATH)

    def __init__(self):
        self.config = configs.BaseConfig()
        self.config.display()
        self.dataset_train = FoodMask60(self.FEEDER_DIR, train=True)
        self.dataset_val = FoodMask60(self.FEEDER_DIR, train=False)
        # Display some images of the food dataset
        # self.dataset_train.display(1)
        self.inference_config = configs.InferenceConfig()
        self.model = None

    def build(self, mode, logs_dir=None):
        '''
            mode: training/ inference
        '''
        if not logs_dir:
            logs_dir = self.LOGS_DIR
        if mode == 'inference':
            config = self.inference_config
        elif mode == 'training':
            config = self.config
        model = modellib.MaskRCNN(
            mode=mode, config=config, model_dir=logs_dir)
        self.model = model
        return model

    def load_weights(self, path=None, special='default'):
        # if special.lower() == "imagenet":
        #     model.load_weights(model.get_imagenet_weights(), by_name=True)

        # path = model.find_last()
        if special.lower() == 'coco':
            self.model.load_weights(self.COCO_MODEL_PATH, by_name=True,
                                    exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",
                                             "mrcnn_bbox", "mrcnn_mask"])
        elif special.lower() == 'last':
            self.model.load_weights(self.model.find_last(), by_name=True)
        else:
            self.model.load_weights(path, by_name=True)

    def train(self, epoch=1, learning_rate_coefficient=0.1, layers="all"):
        """
                layers: Allows selecting wich layers to train. It can be:
                - A regular expression to match layer names to train
                - One of these predefined values:
                heads: The RPN, classifier and mask heads of the network
                all: All the layers
                3+: Train Resnet stage 3 and up
                4+: Train Resnet stage 4 and up
                5+: Train Resnet stage 5 and up
        """

        # Fine tune all layers
        # Passing layers="all" trains all layers. You can also
        # pass a regular expression to select which layers to
        # train by name pattern.
        self.model.train(self.dataset_train, self.dataset_val,
                         learning_rate=self.config.LEARNING_RATE * learning_rate_coefficient,
                         epochs=epoch,
                         layers=layers)

        # Train the head branches
        # Passing layers="heads" freezes all layers except the head
        # layers. You can also pass a regular expression to select
        # which layers to train by name pattern.
        # model.train(dataset_train, dataset_val,
        #             learning_rate=config.LEARNING_RATE,
        #             epochs=epoch_stage2,
        #             layers='heads')

    def random_img(self, display=False):
        # Test on a random image
        image_id = random.choice(self.dataset_val.image_ids)
        original_image, image_meta, gt_class_id, gt_bbox, gt_mask = \
            modellib.load_image_gt(self.dataset_val, self.inference_config,
                                   image_id, use_mini_mask=False)

        if display:
            log("original_image", original_image)
            log("image_meta", image_meta)
            log("gt_class_id", gt_class_id)
            log("gt_bbox", gt_bbox)
            log("gt_mask", gt_mask)
            visualize.display_instances(original_image, gt_bbox, gt_mask, gt_class_id,
                                        self.dataset_train.class_names, figsize=(8, 8))
        return original_image

    @staticmethod
    def resize_image(input_img, output_img, width=128, height=128):
        from PIL import Image
        img = Image.open(input_img)
        try:
            img = img.resize((width, height), Image.BILINEAR)
            img.save(output_img)
            img.close()
        except Exception as e:
            print(e)

    @staticmethod
    def read_image(path):
        """Load the specified image and return a [H,W,3] Numpy array.
        """
        import skimage
        # Load image
        image = skimage.io.imread(path)
        # If grayscale. Convert to RGB for consistency.
        if image.ndim != 3:
            image = skimage.color.gray2rgb(image)
        # If has an alpha channel, remove it for consistency
        if image.shape[-1] == 4:
            image = image[..., :3]
        return image

    def mold_inputs(self, images):
        return self.model.mold_inputs(images)

    def get_extra_infos(self, images):
        molded_images, image_metas, _ = self.mold_inputs(images)
        # Validate image sizes
        # All images in a batch MUST be of the same size
        image_shape = molded_images[0].shape
        for g in molded_images[1:]:
            assert g.shape == image_shape,\
                "After resizing, all images must have the same size. Check IMAGE_RESIZE_MODE and image sizes."

        # Anchors
        anchors = self.model.get_anchors(image_shape)
        # Duplicate across the batch dimension because Keras requires it
        # TODO: can this be optimized to avoid duplicating the anchors?
        anchors = np.broadcast_to(
            anchors, (self.model.config.BATCH_SIZE,) + anchors.shape)
        return molded_images, image_metas, anchors

    def detect(self, img):
        # Get path to saved weights
        # Either set a specific path or find last trained weights
        # model_path = os.path.join(ROOT_DIR, ".h5 file name here")
        results = self.model.detect([img], verbose=1)
        r = results[0]
        visualize.display_instances(
            img, r['rois'], r['masks'], r['class_ids'], self.dataset_val.class_names,
            r['scores'])

    def eval(self):
        # Compute VOC-Style mAP @ IoU=0.5
        # Running on 10 images. Increase for better accuracy.
        # image_ids = np.random.choice(self.dataset_val.image_ids, 10)
        image_ids = self.dataset_val.image_ids
        APs = []
        for image_id in image_ids:
            # Load image and ground truth data
            image, image_meta, gt_class_id, gt_bbox, gt_mask = \
                modellib.load_image_gt(self.dataset_val, self.inference_config,
                                       image_id, use_mini_mask=False)
            # molded_images = np.expand_dims(
            #     modellib.mold_image(image, self.inference_config), 0)
            # Run object detection
            results = self.model.detect([image], verbose=0)
            r = results[0]
            # Compute AP
            AP, precisions, recalls, overlaps = \
                utils.compute_ap(gt_bbox, gt_class_id, gt_mask,
                                 r["rois"], r["class_ids"], r["scores"], r['masks'])
            APs.append(AP)
        m_ap = np.mean(APs)
        return m_ap
