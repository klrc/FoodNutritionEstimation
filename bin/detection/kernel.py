from .FoodMask60 import FoodMask60
from .model_def import model as modellib
from .model_def.config import Config as BaseConfig
import datetime


class TrainConfig(BaseConfig):
    # Give the configuration a recognizable name
    NAME = "detection"

    # Train on 1 GPU and 8 images per GPU. We can put multiple images on each
    # GPU because the images are small. Batch size is 8 (GPUs * images/GPU).
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1

    LOSS_WEIGHTS = {
        "rpn_class_loss": 0.8,
        "rpn_bbox_loss": 0.8,
        "mrcnn_class_loss": 1.,
        "mrcnn_bbox_loss": 0.8,
        "mrcnn_mask_loss": 0.8
    }

    # Number of classes (including background)
    NUM_CLASSES = 31  # background + 3 shapes

    # Use small images for faster training. Set the limits of the small side
    # the large side, and that determines the image shape.
    IMAGE_MIN_DIM = 128
    IMAGE_MAX_DIM = 128

    # Use smaller anchors because our image and objects are small
    RPN_ANCHOR_SCALES = (8, 16, 32, 64, 128)  # anchor side in pixels

    # Reduce training ROIs per image because the images are small and have
    # few objects. Aim to allow ROI sampling to pick 33% positive ROIs.
    TRAIN_ROIS_PER_IMAGE = 32

    # Use a small epoch since the data is simple
    STEPS_PER_EPOCH = 100

    # use small validation steps since the epoch is small
    VALIDATION_STEPS = 5


class InfConfig(TrainConfig):
    DETECTION_NMS_THRESHOLD = 0.01
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1


class Kernel(InfConfig):

    model = None
    mode = 'inference'

    dataset_train = None
    dataset_val = None

    TRAIN_EPOCHS = 10
    CHECKPOINT_INTERVAL = 5
    VERBOSE = True

    DISPLAY_SELF = True
    TRAIN_LAYERS = 'all'

    BUILD_DIR = '.build/MaskRCNN'
    FEEDER_DIR = '.build/FoodMask60'
    LOAD_PATH = 'coco'
    COCO_MODEL_PATH = '.downloads/mask_rcnn_coco.h5'
    LOG_PATH = '.log/Detection.log'

    def __init__(self):
        self.dataset_train = FoodMask60(self.FEEDER_DIR, for_train=True)
        self.dataset_val = FoodMask60(self.FEEDER_DIR, for_train=False)
        self.build()
        self.load()

        if self.DISPLAY_SELF:
            self.display()

    def log(self, command, **kwargs):
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        msgs = [f"[{t}]\t{command}:"]
        msgs.extend([f'\t\t\t{key} = {kwargs[key]}' for key in kwargs])
        with open(self.LOG_PATH, 'a') as f:
            for msg in msgs:
                f.write(f'{msg}\n')

    def build(self, mode=None):
        '''
            mode: training/ inference
        '''
        if not mode:
            mode = self.mode
        if mode == 'inference':
            config = InfConfig()
        elif mode == 'training':
            config = TrainConfig()
        model = modellib.MaskRCNN(
            mode=mode, config=config, model_dir=self.BUILD_DIR)
        self.model = model
        self.log(f'build MaskRCNN in {mode} mode')

    def load(self, path=None):
        # if special.lower() == "imagenet":
        #     model.load_weights(model.get_imagenet_weights(), by_name=True)

        # path = model.find_last()
        if not path:
            path = self.LOAD_PATH
        self.log(f'load weights from {path}')
        if path == 'coco':
            self.model.load_weights(self.COCO_MODEL_PATH, by_name=True,
                                    exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",
                                             "mrcnn_bbox", "mrcnn_mask"])
        elif path == 'last':
            self.model.load_weights(self.model.find_last(), by_name=True)
        else:
            self.model.load_weights(path, by_name=True)

    def display(self):
        """Display Configuration values."""
        info = "\nConfigurations:\n"
        for a in dir(self):
            if not a.startswith("__") and not callable(getattr(self, a)):
                info += "{:30} {}\n".format(a, getattr(self, a))
        print(info)
        self.log(info)

    def inference(self, x):
        results = self.model.detect([x], verbose=1 if self.VERBOSE else 0)
        return results[0]

    def train(self):
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
        self.build('training')
        self.load()

        self.log('training started', learning_rate=self.LEARNING_RATE)
        self.model.train(self.dataset_train, self.dataset_val,
                         learning_rate=self.LEARNING_RATE,
                         epochs=self.TRAIN_EPOCHS,
                         layers=self.TRAIN_LAYERS, period=self.CHECKPOINT_INTERVAL)
        self.log(f'training finished with {self.TRAIN_EPOCHS} epochs.')
