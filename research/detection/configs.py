from .mrcnn.config import Config


class BaseConfig(Config):
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


class InferenceConfig(BaseConfig):
    DETECTION_NMS_THRESHOLD = 0.01
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1
