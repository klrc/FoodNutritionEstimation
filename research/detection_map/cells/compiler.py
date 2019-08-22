

import json
import os
import PIL.Image as Image
import yaml
from labelme import utils
import shutil
import math
import base64
import Augmentor
import numpy as np
import random

from .dataset import FoodMask60


class Compiler():
    def __init__(self, target_dir):
        self.target_dir = target_dir

    def build_record(self, src, size=None):

        # load json data
        with open(src, 'r', encoding='gbk') as f:
            data = json.load(f)

        # load img
        if data['imageData']:
            imageData = data['imageData']
        else:
            imagePath = os.path.join(
                os.path.dirname(src), data['imagePath'])
            with open(imagePath, 'rb') as f:
                imageData = f.read()
                imageData = base64.b64encode(imageData).decode('utf-8')
        _img = utils.img_b64_to_arr(imageData)

        # load mask & info
        label_name_to_value = {'_background_': 0}
        for shape in data['shapes']:
            label_name = shape['label']
            if label_name in label_name_to_value:
                label_value = label_name_to_value[label_name]
            else:
                label_value = len(label_name_to_value)
                label_name_to_value[label_name] = label_value
        label_values, label_names = [], []
        for ln, lv in sorted(label_name_to_value.items(), key=lambda x: x[1]):
            label_values.append(lv)
            label_names.append(ln)
        assert label_values == list(range(len(label_values)))
        _mask = utils.shapes_to_label(
            _img.shape, data['shapes'], label_name_to_value)
        _info = dict(label_names=label_names)

        # save builds
        _hash = src.split('/')[-1].split('.')[0]
        _path = f'{self.target_dir}/records.pak/{_hash}'
        if not os.path.exists(_path):
            os.makedirs(_path)

        _img = Image.fromarray(_img)
        if size:
            _img = _img.resize(size, Image.BILINEAR)
        _img.save(os.path.join(_path, 'img.png'))
        utils.lblsave(os.path.join(_path, 'mask.png'), _mask)
        with open(os.path.join(_path, 'info.yaml'), 'w') as f:
            yaml.safe_dump(_info, f, default_flow_style=False)

        return _path

    def augmentor(self, records, batch=8):

        # build imgs pipeline
        _imgs = []
        for record in records:
            raw = np.asarray(Image.open(f'{record}/img.png'))
            mask = np.asarray(Image.open(f'{record}/mask.png'))
            _imgs.append([raw, mask])

        # save record path as label
        p = Augmentor.DataPipeline(_imgs, labels=records)
        p.rotate(1, max_left_rotation=5, max_right_rotation=5)
        p.flip_top_bottom(0.5)
        p.zoom_random(1, percentage_area=0.5)

        aug_records, labels = p.sample(batch-1)
        for index, ((raw, mask), src) in enumerate(zip(aug_records, labels)):
            _hash = src.split('/')[-1].split('.')[0]
            _new_hash = f'AUG{index}f_{_hash}'
            _path = src.replace(_hash, _new_hash)
            if not os.path.exists(_path):
                os.makedirs(_path)

            Image.fromarray(raw).save(f'{_path}/img.png')
            Image.fromarray(mask).save(f'{_path}/mask.png')
            shutil.copy(f'{src}/info.yaml', f'{_path}/info.yaml')
            yield _path

    def __from_yaml_read_cls(self, yaml_path):
        with open(yaml_path) as f:
            temp = yaml.load(f.read(), Loader=yaml.FullLoader)
            labels = temp['label_names']
            del labels[0]
        return labels[0]

    def build_dataset(self, records, val_prop=0.1, seed=1):

        # set random seed
        random.seed(seed)

        # split as groups
        record_groups = {}
        for record in records:
            key = self.__from_yaml_read_cls(f'{record}/info.yaml')
            if key not in record_groups.keys():
                record_groups[key] = []
            record_groups[key].append(record)

        _train, _val = [], []

        # build train & val for each group
        for key in record_groups.keys():
            group = record_groups[key]
            val = random.sample(group, math.ceil(len(group)*val_prop))
            train = [x for x in group if x not in val]
            _train.extend(train)
            _val.extend(val)

        size = Image.open(f'{records[0]}/img.png').size
        return FoodMask60(_train, size), FoodMask60(_val, size)
