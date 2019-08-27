import datetime
from .mask_rcnn_utils.config import Config
from .mask_rcnn_utils import model as modellib

import os
import keras
import json
import sys
sys.path.append('.')


class EvolutionConfig(Config):
    # Give the configuration a recognizable name
    NAME = "detection"

    # Train on 1 GPU and 8 images per GPU. We can put multiple images on each
    # GPU because the images are small. Batch size is 8 (GPUs * images/GPU).
    GPU_COUNT = 1
    IMAGES_PER_GPU = 4

    LOSS_WEIGHTS = {
        "rpn_class_loss": 1,
        "rpn_bbox_loss": 0.8,
        "mrcnn_class_loss": 1.,
        "mrcnn_bbox_loss": 0.8,
        "mrcnn_mask_loss": 0.8
    }

    # Number of classes (including background)
    NUM_CLASSES = 79  # background + 3 shapes

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


class InferenceConfig(EvolutionConfig):
    DETECTION_NMS_THRESHOLD = 0.01
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1


class MaskRCNN():
    @staticmethod
    def compile(mode):
        if mode == 'inference':
            compile_config = InferenceConfig()
        elif mode == 'training':
            compile_config = EvolutionConfig()
        compile_config.display()
        model = modellib.MaskRCNN(mode=mode, config=compile_config)
        return Network(model)


class Network():
    def __init__(self, model):
        self.model = model

    def evolution(self, model, config, train, val):
        dir_name = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
        model_hash = f"MRCNND_{config['hash']}_{dir_name}"
        log_dir = f"{config['log_dir']}/{model_hash}"
        checkpoint_path = config['checkpoint_path'].replace(
            'mask_rcnn_detection', model_hash)
        if not os.path.exists(log_dir):
            os.makedirs(log_dir)
        # Callbacks
        callbacks = [
            keras.callbacks.TensorBoard(log_dir=log_dir,
                                        histogram_freq=0, write_graph=False, write_images=False, update_freq=16),
            keras.callbacks.ModelCheckpoint(checkpoint_path,
                                            verbose=0, save_weights_only=True, period=config['period']),
        ]
        self.model.train(train_dataset=train,
                         val_dataset=val,
                         learning_rate=config['learning_rate'],
                         epochs=config['epochs'],
                         layers=config['layers'],
                         callbacks=callbacks)

    def test_evolutions(self, configs, train, val):
        for config in configs:
            coco_path = config['coco_path']
            start_from = config['start_from']

            if start_from == "coco":
                self.model.load_weights(coco_path, by_name=True,
                                        exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",
                                                 "mrcnn_bbox", "mrcnn_mask"])
            else:
                self.model.load_weights(start_from, by_name=True)
            self.evolution(self.model, config, train, val)
