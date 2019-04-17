
import os

import numpy as np
import yaml
from PIL import Image

from mrcnn import utils, visualize


class FoodDataset(utils.Dataset):
    label_list = ['dumpling', 'tcpg', 'sm', 'qzly', 'potato', 'qchx', 'beefpotato', 'noodles', 'bread', 'mdcsg',
                  'mdcrs', 'gbrice', 'khs', 'currybeef', 'beef', 'hsyk', 'hstddpg', 'hspg', 'hsjy', 'hsjt',
                  'chiken', 'hsdy', 'hsdp', 'dtj', 'cyszx', 'cdj', 'crht', 'bdcrs', 'bun', 'bzhx']

    def __init__(self, path, size=(200, 150), train=True):
        super().__init__()
        # Add classes
        for i, x in enumerate(self.label_list):
            self.add_class('food', i + 1, x)
        self.load_info(path, size, train)
        self.prepare()

    def get_dirs(path, train):
        path = f'{path}/{mark}'
        img_dir = f'{path}/img'  # 存放原图片的文件夹
        mask_dir = f'{path}/mask'
        yaml_dir = f'{path}/yaml'
        return img_dir, mask_dir, yaml_dir

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

    def load_info(self, path, size, train):
        """Generate the requested number of synthetic images.
        count: number of images to generate.
        height, width: the size of the generated images.
        """
        mark = 'train' if train else 'test'
        path = f'{path}/{mark}'
        imglist = os.listdir(f'{path}/img')
        for index, img in enumerate(imglist):
            img_id = imglist[index].split(".")[0]
            img_path = f'{path}/img/{img_id}.png'
            mask_path = f'{path}/mask/{img_id}.png'
            yaml_path = f'{path}/yaml/{img_id}.yaml'
            self.add_image(source="food", image_id=index, path=img_path,
                           width=size[0], height=size[1], mask_path=mask_path, yaml_path=yaml_path)

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

    def display(self, num=4):
        # Load and display random samples
        image_ids = np.random.choice(self.image_ids, num)
        for image_id in image_ids:
            image = self.load_image(image_id)
            mask, class_ids = self.load_mask(image_id)
            visualize.display_top_masks(
                image, mask, class_ids, self.class_names)


if __name__ == "__main__":
    feeder_path = 'detection/__feeder__'
    ds = FoodDataset(path=feeder_path)
    ds.display()
    print('finish')
