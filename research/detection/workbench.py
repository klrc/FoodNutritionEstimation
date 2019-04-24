import random
import numpy as np

from . import configs
from .dataset import FoodDataset
from .mrcnn import utils, visualize
from .mrcnn import model as modellib
from .mrcnn.model import log

# # %matplotlib inline
# inter_num = 0
# np.set_printoptions(threshold=np.inf)


class Workbench():
    FEEDER_DIR = 'detection/__feeder__'
    # Directory to save logs and trained model
    MODEL_DIR = 'detection/__logs__'
    # Local path to trained weights file
    COCO_MODEL_PATH = 'data/mask_rcnn_coco.h5'
    # Download COCO trained weights from Releases if needed
    # if not os.path.exists(COCO_MODEL_PATH):
    #     utils.download_trained_weights(COCO_MODEL_PATH)

    def __init__(self):
        self.config = configs.BaseConfig()
        self.config.display()
        self.dataset_train = FoodDataset(self.FEEDER_DIR, train=True)
        self.dataset_val = FoodDataset(self.FEEDER_DIR, train=False)
        # Display some images of the food dataset
        # self.dataset_train.display(1)
        self.inference_config = configs.InferenceConfig()
        self.model = None

    def init_model(self, mode='init', model_dir=None, path=None):
        """
            mode:
                'init': start with COCO pretrained model.
                'training': load the last model you trained and continue training.
                'inference': load saved weights for inference.
        """
        if not model_dir:
            model_dir = self.MODEL_DIR
        if mode == 'init':
            # Create model in training mode
            model = modellib.MaskRCNN(
                mode="training", config=self.config, model_dir=model_dir)
            
            # Which weights to start with?
            init_with = "coco"  # imagenet, coco
            if init_with == "imagenet":
                model.load_weights(model.get_imagenet_weights(), by_name=True)
            elif init_with == "coco":
                # Load weights trained on MS COCO, but skip layers that
                # are different due to the different number of classes
                # See README for instructions to download the COCO weights
                model.load_weights(self.COCO_MODEL_PATH, by_name=True,
                                   exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",
                                            "mrcnn_bbox", "mrcnn_mask"])
        elif mode == 'training':
            model = modellib.MaskRCNN(
                mode="training", config=self.config, model_dir=model_dir)
            model.load_weights(model.find_last(), by_name=True)
        elif mode == 'inference':
            # Recreate the model in inference mode
            model = modellib.MaskRCNN(
                mode="inference", config=self.inference_config, model_dir=model_dir)
            model.load_weights(path, by_name=True)
        self.model = model
        return model

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
    def resize(input_img, output_img, width=128, height=128):
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
        image_ids = np.random.choice(self.dataset_val.image_ids, 10)
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
        return f'mAP: {np.mean(APs)}'
