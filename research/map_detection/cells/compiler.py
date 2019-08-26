

import json
import os
import PIL.Image as Image
import yaml
from labelme import utils
from labelme.utils.draw import label_colormap
import shutil
import math
import base64
import Augmentor
import numpy as np
import random


class Compiler():
    def __init__(self, target_dir):
        if not os.path.exists(target_dir):
            os.makedirs(target_dir)
        self.target_dir = target_dir

    def collect(self, src):
        for root, dirs, files in os.walk(src):
            for _file in files:
                if _file.endswith('.json'):
                    yield f'{root}/{_file}'

    def __replace_label(self, label):
        if label == 'beef':
            return 'jnr'
        elif label == 'chiken':
            return 'hsjk'
        elif label == 'potato':
            return 'qctds'
        elif label == 'beefpotato':
            return 'tddnr'
        elif label == 'currybeef':
            return 'glnr'
        elif label == 'bun':
            return 'bz'
        else:
            return label

    def build_records(self, srcs, size=None):
        for src in srcs:
            print(f'building src <{src}> ..')
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
                shape['label'] = label_name = self.__replace_label(label_name)
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

            # convert to imgs
            _img = Image.fromarray(_img)
            _mask = Image.fromarray(_mask.astype(np.uint8), mode='P')

            if size:
                _img = _img.resize(size, Image.BILINEAR)
                _mask = _mask.resize(size, Image.BILINEAR)

            colormap = label_colormap(255)
            _mask.putpalette((colormap * 255).astype(np.uint8).flatten())

            # save builds
            _hash = hash(src)
            _path = f'{self.target_dir}/records.pak/{_hash}'
            if not os.path.exists(_path):
                os.makedirs(_path)

            _img.save(os.path.join(_path, 'img.png'))
            _mask.save(f'{_path}/mask.png')
            with open(os.path.join(_path, 'info.yaml'), 'w') as f:
                yaml.safe_dump(_info, f, default_flow_style=False)

            yield _path

    def augments(self, records, batch=8):
        colormap = label_colormap(255)
        for record in records:
            yield record
            raw = np.asarray(Image.open(f'{record}/img.png'))
            mask = np.asarray(Image.open(f'{record}/mask.png'))

            # save record path as label
            p = Augmentor.DataPipeline([[raw, mask]])
            p.rotate(1, max_left_rotation=5, max_right_rotation=5)
            p.flip_top_bottom(0.5)
            p.zoom_random(1, percentage_area=0.5)

            aug_records = p.sample(batch-1)

            for index, (raw, mask) in enumerate(aug_records):
                print(
                    f'augmenting <{record}> [{index+1}/{len(aug_records)}]..')
                _hash = record.split('/')[-1].split('.')[0]
                _new_hash = f'AUG{index}f_{_hash}'
                _path = record.replace(_hash, _new_hash)
                if not os.path.exists(_path):
                    os.makedirs(_path)

                Image.fromarray(raw).save(f'{_path}/img.png')

                _mask = Image.fromarray(mask)
                _mask.putpalette((colormap * 255).astype(np.uint8).flatten())
                _mask.save(f'{_path}/mask.png')

                shutil.copy(f'{record}/info.yaml', f'{_path}/info.yaml')
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
        print('reading groups ..')
        record_groups = {}
        for record in records:
            key = self.__from_yaml_read_cls(f'{record}/info.yaml')
            if key not in record_groups.keys():
                record_groups[key] = []
            record_groups[key].append(record)

        _train, _val = [], []

        # build train & val for each group
        print('compiling each group as train/val ..')
        for key in record_groups.keys():
            group = record_groups[key]
            val = random.sample(group, math.ceil(len(group)*val_prop))
            train = [x for x in group if x not in val]
            _train.extend(train)
            _val.extend(val)

        return _train, _val
