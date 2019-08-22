import numpy as np
import yaml
from PIL import Image

from .mask_rcnn_utils import utils, visualize


class FoodMask60(utils.Dataset):

    def __init__(self, records, size):
        super().__init__()
        self.load_images(records, size)
        self.load_classes()
        self.prepare()

    def load_classes(self):
        # Add classes
        label_list = set()
        for index, image in enumerate(self.image_info):
            label = self.from_yaml_get_class(index)[0]
            label_list.add(label)
        for i, _class in enumerate(label_list):
            self.add_class('food', i + 1, _class)

    def load_images(self, records, size):
        """Generate the requested number of synthetic images.
        height, width: the size of the generated images.
        for_train: gen trainset or testset.
        """
        for record in records:
            _hash = record.split('/')[-1]
            self.add_image(
                source="food",
                image_id=_hash,
                width=size[0],
                height=size[1],
                path=f'{record}/img.png',
                mask=f'{record}/mask.png',
                yaml=f'{record}/info.yaml',
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
            temp = yaml.load(f.read(), Loader=yaml.FullLoader)
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
