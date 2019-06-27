import os
import numpy as np
import yaml
from PIL import Image

from .model_def import utils, visualize


class FoodMask60(utils.Dataset):
    label_list = ['bdcrs', 'beef', 'beefpotato', 'bread', 'bun', 'bzhx', 'cdj', 'chiken', 'crht', 'currybeef', 'cyszx', 'dtj', 'dumpling', 'gbrice',
                  'hsdp', 'hsdy', 'hsjt', 'hsjy', 'hspg', 'hstddpg', 'hsyk', 'khs', 'mdcrs', 'mdcsg', 'noodles', 'potato', 'qchx', 'qzly', 'sm', 'tcpg']

    def __init__(self, path, width=200, height=150, for_train=True):
        super().__init__()
        self.load_classes()
        self.load_images(path, width, height, for_train)
        self.prepare()

    def load_classes(self):
        # Add classes
        for i, _class in enumerate(self.label_list):
            self.add_class('food', i + 1, _class)

    def load_images(self, path, width, height, for_train):
        """Generate the requested number of synthetic images.
        path: built dataset dir.
        height, width: the size of the generated images.
        for_train: gen trainset or testset.
        """
        mark = 'train' if for_train else 'test'
        path = f'{path}/{mark}'
        imglist = os.listdir(path)
        for index, _hash in enumerate(imglist):
            self.add_image(
                source="food",
                image_id=index,
                width=width,
                height=height,
                path=f'{path}/{_hash}/raw.png',
                mask=f'{path}/{_hash}/mask.png',
                yaml=f'{path}/{_hash}/yaml.yaml',
                hash=_hash,
            )

    # override
    def load_mask(self, image_id):
        """Generate instance masks for shapes of the given image ID.
        """
        _info = self.image_info[image_id]
        count = 1  # number of object ?
        _image = Image.open(_info['mask'])
        num_obj = np.max(_image)

        # get masks
        mask = np.zeros(
            [_info['height'], _info['width'], num_obj], dtype=np.uint8)
        mask = self.draw_mask(num_obj, mask, _image, image_id)
        occlusion = np.logical_not(mask[:, :, -1]).astype(np.uint8)
        for i in range(count - 2, -1, -1):
            mask[:, :, i] = mask[:, :, i] * occlusion
            occlusion = np.logical_and(
                occlusion, np.logical_not(mask[:, :, i]))

        # get labels
        _labels = self.from_yaml_get_class(image_id)
        labels_form = []
        for i in range(len(_labels)):
            for x in self.label_list:
                if _labels[i].find(x) != -1:
                    labels_form.append(x)
                    break
        class_ids = np.array([self.class_names.index(s) for s in labels_form])
        return mask, class_ids.astype(np.int32)

    def from_yaml_get_class(self, image_id):
        info = self.image_info[image_id]
        with open(info['yaml']) as f:
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

    def display(self, num=4):
        # Load and display random samples
        image_ids = np.random.choice(self.image_ids, num)
        for image_id in image_ids:

            image = self.load_image(image_id)
            mask, class_ids = self.load_mask(image_id)
            visualize.display_top_masks(
                image, mask, class_ids, self.class_names)
