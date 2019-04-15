import os
import sys
import random
import math
import re
import time
import numpy as np
import cv2
import matplotlib
import matplotlib.pyplot as plt
from PIL import Image
import yaml
from os import listdir
from mrcnn.config import Config
from mrcnn import utils
import mrcnn.model as modellib
from mrcnn import visualize
from mrcnn.model import log

# %matplotlib inline
inter_num = 0
np.set_printoptions(threshold=np.inf)

feeder_path = 'detection/__feeder__'
sys.path.append('.')

def _feeder_dirs(train=True):
    mark = 'train' if train else 'test'
    path = f'{feeder_path}/{mark}'
    img_dir = f'{path}/img'  # 存放原图片的文件夹
    mask_dir = f'{path}/mask'
    yaml_dir = f'{path}/yaml'
    return img_dir, mask_dir, yaml_dir


# Directory to save logs and trained model
MODEL_DIR = 'detection/__logs__'

# Local path to trained weights file
COCO_MODEL_PATH = 'data/mask_rcnn_coco.h5'
# Download COCO trained weights from Releases if needed
if not os.path.exists(COCO_MODEL_PATH):
    utils.download_trained_weights(COCO_MODEL_PATH)


# count = len(imglist)  # 文件的数目
IMG_WIDTH = 200
IMG_HEIGHT = 150

GPU_COUNT = 1
IMAGES_PER_GPU = 1


class ShapesConfig(Config):
    """Configuration for training on the toy shapes dataset.
    Derives from the base Config class and overrides values specific
    to the toy shapes dataset.
    """
    # Give the configuration a recognizable name
    NAME = "shapes"

    # Train on 1 GPU and 8 images per GPU. We can put multiple images on each
    # GPU because the images are small. Batch size is 8 (GPUs * images/GPU).
    GPU_COUNT = GPU_COUNT
    IMAGES_PER_GPU = IMAGES_PER_GPU

    # Number of classes (including background)
    NUM_CLASSES = 1 + 30  # background + 3 shapes

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


config = ShapesConfig()
config.display()


class DrugDataset(utils.Dataset):
    label_list = ['dumpling', 'tcpg', 'sm', 'qzly', 'potato', 'qchx', 'beefpotato', 'noodles', 'bread', 'mdcsg',
                  'mdcrs', 'gbrice', 'khs', 'currybeef', 'beef', 'hsyk', 'hstddpg', 'hspg', 'hsjy', 'hsjt',
                  'chiken', 'hsdy', 'hsdp', 'dtj', 'cyszx', 'cdj', 'crht', 'bdcrs', 'bun', 'bzhx']

    def get_obj_index(self, image):
        n = np.max(image)
        return n

    def from_yaml_get_class(self, image_id):
        info = self.image_info[image_id]
        with open(info['yaml_path']) as f:
            temp = yaml.load(f.read())
            labels = temp['label_names']
            del labels[0]
        return labels

    def draw_mask(self, num_obj, mask, image, image_id):
        info = self.image_info[image_id]
        for index in range(num_obj):
            for i in range(info['width']):
                for j in range(info['height']):
                    at_pixel = image.getpixel((i, j))
                    if at_pixel == index + 1:
                        mask[j, i, index] = 1
        return mask

    def load_shapes(self, height, width, img_folder, mask_folder, yaml_folder):
        """Generate the requested number of synthetic images.
        count: number of images to generate.
        height, width: the size of the generated images.
        """
        # Add classes
        for i, x in enumerate(self.label_list):
            self.add_class('shapes', i + 1, x)
        imglist = listdir(img_folder)
        for index, img in enumerate(imglist):
            img_id = imglist[index].split(".")[0]
            mask_path = ''
            for x in os.listdir(mask_folder):
                if x.startswith(img_id):
                    mask_path = mask_folder + "/" + x
            yaml_path = f'{yaml_folder}/{img_id}.yaml'
            self.add_image("shapes", image_id=index, path=f'{img_folder}/{img_id}.png',
                           width=width, height=height, mask_path=mask_path, yaml_path=yaml_path)

    def load_mask(self, image_id):
        """Generate instance masks for shapes of the given image ID.
        """
        info = self.image_info[image_id]
        count = 1  # number of object
        img = Image.open(info['mask_path'])
        num_obj = self.get_obj_index(img)
        mask = np.zeros(
            [info['height'], info['width'], num_obj], dtype=np.uint8)
        mask = self.draw_mask(num_obj, mask, img, image_id)
        occlusion = np.logical_not(mask[:, :, -1]).astype(np.uint8)
        for i in range(count - 2, -1, -1):
            mask[:, :, i] = mask[:, :, i] * occlusion
            occlusion = np.logical_and(
                occlusion, np.logical_not(mask[:, :, i]))
        labels = self.from_yaml_get_class(image_id)
        labels_form = []

        for i in range(len(labels)):
            for x in self.label_list:
                if labels[i].find(x) != -1:
                    labels_form.append(x)
                    break

        class_ids = np.array([self.class_names.index(s) for s in labels_form])
        return mask, class_ids.astype(np.int32)


def prepare_dataset():
    # train与val数据集准备
    dataset_train = DrugDataset()
    dataset_train.load_shapes(IMG_HEIGHT, IMG_WIDTH, *_feeder_dirs(train=True))
    dataset_train.prepare()

    dataset_val = DrugDataset()
    dataset_val.load_shapes(IMG_HEIGHT, IMG_WIDTH, *_feeder_dirs(train=False))
    dataset_val.prepare()
    return dataset_train, dataset_val


dataset_train, dataset_val = prepare_dataset()


def display_samples(num=4):
    # Load and display random samples
    image_ids = np.random.choice(dataset_train.image_ids, num)
    for image_id in image_ids:
        image = dataset_train.load_image(image_id)
        mask, class_ids = dataset_train.load_mask(image_id)
        visualize.display_top_masks(
            image, mask, class_ids, dataset_train.class_names)


def train(epoch_stage1=2, epoch_stage2=1):
    # Create model in training mode
    model = modellib.MaskRCNN(mode="training", config=config,
                              model_dir=MODEL_DIR)

    # Which weights to start with?
    init_with = "coco"  # imagenet, coco, or last

    if init_with == "imagenet":
        model.load_weights(model.get_imagenet_weights(), by_name=True)
    elif init_with == "coco":
        # Load weights trained on MS COCO, but skip layers that
        # are different due to the different number of classes
        # See README for instructions to download the COCO weights
        model.load_weights(COCO_MODEL_PATH, by_name=True,
                           exclude=["mrcnn_class_logits", "mrcnn_bbox_fc",
                                    "mrcnn_bbox", "mrcnn_mask"])
    elif init_with == "last":
        # Load the last model you trained and continue training
        model.load_weights(model.find_last(), by_name=True)

    # Fine tune all layers
    # Passing layers="all" trains all layers. You can also
    # pass a regular expression to select which layers to
    # train by name pattern.
    model.train(dataset_train, dataset_val,
                learning_rate=config.LEARNING_RATE / 10,
                epochs=epoch_stage1,
                layers="all")

    # Train the head branches
    # Passing layers="heads" freezes all layers except the head
    # layers. You can also pass a regular expression to select
    # which layers to train by name pattern.
    model.train(dataset_train, dataset_val,
                learning_rate=config.LEARNING_RATE,
                epochs=epoch_stage2,
                layers='heads')


class InferenceConfig(ShapesConfig):
    GPU_COUNT = GPU_COUNT
    IMAGES_PER_GPU = IMAGES_PER_GPU


inference_config = InferenceConfig()
# Recreate the model in inference mode
model = modellib.MaskRCNN(
    mode="inference", config=inference_config, model_dir=f'{MODEL_DIR}')


def random_img():
    # Test on a random image
    image_id = random.choice(dataset_val.image_ids)
    original_image, image_meta, gt_class_id, gt_bbox, gt_mask = \
        modellib.load_image_gt(dataset_val, inference_config,
                               image_id, use_mini_mask=False)

    log("original_image", original_image)
    log("image_meta", image_meta)
    log("gt_class_id", gt_class_id)
    log("gt_bbox", gt_bbox)
    log("gt_mask", gt_mask)
    visualize.display_instances(original_image, gt_bbox, gt_mask, gt_class_id,
                                dataset_train.class_names, figsize=(8, 8))
    return original_image


def detect(model, img):
    # Get path to saved weights
    # Either set a specific path or find last trained weights
    # model_path = os.path.join(ROOT_DIR, ".h5 file name here")
    results = model.detect([img], verbose=1)
    r = results[0]
    visualize.display_instances(
        img, r['rois'], r['masks'], r['class_ids'], dataset_val.class_names,
        r['scores'])


def _get_ax(rows=1, cols=1, size=8):
    """Return a Matplotlib Axes array to be used in
    all visualizations in the notebook. Provide a
    central point to control graph sizes.

    Change the default size attribute to control the size
    of rendered images
    """
    _, ax = plt.subplots(rows, cols, figsize=(size * cols, size * rows))
    return ax


def eval(model):
    # Compute VOC-Style mAP @ IoU=0.5
    # Running on 10 images. Increase for better accuracy.
    image_ids = np.random.choice(dataset_val.image_ids, 10)
    APs = []
    for image_id in image_ids:
        # Load image and ground truth data
        image, image_meta, gt_class_id, gt_bbox, gt_mask = \
            modellib.load_image_gt(dataset_val, inference_config,
                                   image_id, use_mini_mask=False)
        molded_images = np.expand_dims(
            modellib.mold_image(image, inference_config), 0)
        # Run object detection
        results = model.detect([image], verbose=0)
        r = results[0]
        # Compute AP
        AP, precisions, recalls, overlaps = \
            utils.compute_ap(gt_bbox, gt_class_id, gt_mask,
                             r["rois"], r["class_ids"], r["scores"], r['masks'])
        APs.append(AP)

    print("mAP: ", np.mean(APs))


if __name__ == '__main__':

    # display_samples(6)
    train(epoch_stage1=1)
    detect(model)

    # h5s = os.listdir('__eval__')
    # h5s.sort()

    # test_img = random_img()

    # for h5 in h5s:
    #     # if '0.h5' not in h5:
    #         # continue    # eval per 10 ep
    #         # Load trained weights
    #     print("Loading weights from ", h5)
    #     model.load_weights(f'__eval__/{h5}', by_name=True)
    #     detect(model, test_img)
    #     line = str(model._anchor_cache)
    #     import json
    #     with open('test.json', 'w') as f:
    #         json.dump(line, f)
    #     break
