
import inspect
import os
import json
import shutil
import datetime
import random
import yaml
import numpy as np
from PIL import Image
import Augmentor


from .file_utils import exists, rmdir_if_exists, create_if_not_exists, get_dir

# to avoid some bugs.
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True


def _get_func(obj):
    return f'{obj.__class__.__name__}.{inspect.stack()[1][3]}'


class Config():
    """
        Base configuration for database-kernel.
    """

    # basic defs.
    root = 'bin'
    database_id = 'FoodMask60'

    # db dirs.
    database_root = f'{root}/{database_id}'
    database_storage = f'{database_root}/storage'

    # frozen records.
    log_id = f'{database_id}.log'
    core_id = f'{database_id}.core'

    log_path = f'.log/{log_id}'
    core_path = f'{database_root}/{core_id}'

    build_path = f'.build/{database_id}'


class KernelEnv():

    def __init__(self, config: Config):
        self.config = config
        self.data = {}
        self.resources = []

        self.load()

    def log(self, command, verbose=True, **kwargs):
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        msgs = [f"[{t}]\t{command}:"]
        msgs.extend([f'\t\t\t{key} = {kwargs[key]}' for key in kwargs])
        with open(self.config.log_path, 'a') as f:
            for msg in msgs:
                if verbose:
                    print(msg)
                f.write(f'{msg}\n')

    def load(self):
        if exists(self.config.core_path):
            with open(self.config.core_path, 'r') as f:
                core = json.load(f)
                self.__recover_from(core)
                self.log('load frozen core', path=self.config.core_path)
        else:
            self.log('CREATE')

    def __recover_from(self, core):
        self.resources = [tuple(x) for x in core['resources']]
        self.data = core['data']

    def clean_build(self):
        rmdir_if_exists(self.config.build_path)
        self.log(_get_func(self), path=self.config.build_path)

    def clean_resources(self):
        raw_len = len(self.resources)
        self.resources = list(set(self.resources))
        new_len = len(self.resources)
        self.log(_get_func(self), msg=f'{raw_len-new_len} duplicates.')

    def save(self):
        path = self.config.core_path
        create_if_not_exists(get_dir(path))
        with open(path, 'w') as f:
            json.dump(self.freeze(), f)
        self.log('save frozen core', path=path)

    def freeze(self):
        frozen = {
            'resources': self.resources,
            'data': self.data,
        }
        return frozen


class DatabaseKernel(KernelEnv):

    def __init__(self, config: Config):
        super().__init__(config)
        self.log(_get_func(self), msg='online for quests.')

    def __from_yaml_read_cls(self, yaml_path):
        with open(yaml_path) as f:
            temp = yaml.load(f.read())
            labels = temp['label_names']
            del labels[0]
        return labels[0]

    @staticmethod
    def resize(input_img, output_img, width=128, height=128):
        img = Image.open(input_img)
        img = img.resize((width, height), Image.BILINEAR)
        img.save(output_img)
        img.close()

    def __scan_solo(self, address, pattern, _filter, _type):
        if _filter and _filter not in address:
            return None
        if not address.endswith(pattern):
            return None
        _src = address
        address = address.replace(pattern, '')
        _hash = self._get_file(address)
        if _type == 'auto':
            _type = pattern.split('.')[-1]
        return _hash, _type, _src

    def scan(self, path, pattern, _filter=None, _type='auto', recursive=True):
        self.log('SCANNING', path=path)
        counter = 0
        if recursive:
            for x in os.walk(path):
                if len(x[2]) > 0:
                    for item in x[2]:
                        address = f'{x[0]}/{item}'
                        ret = self.__scan_solo(
                            address, pattern, _filter, _type)
                        if ret:
                            counter += 1
                            yield ret
        else:
            for x in os.listdir(path):
                address = f'{path}/{x}'
                ret = self.__scan_solo(address, pattern, _filter)
                if ret:
                    counter += 1
                    yield ret
        self.log('SCANNING finished', results=counter,
                 pattern=pattern, _filter=_filter)

    def auto_scan(self, path, pattern, _filter=None, _type='auto', recursive=True, copy=True, verbose=False):
        for _hash, _type, _src in self.scan(path, pattern, _filter, _type, recursive):
            self.add(_hash, _type, _src, copy=copy, verbose=verbose)
        self.clean_resources()

    def add(self, _hash, _type, _src, copy=False, verbose=True):
        if copy:
            suffix = _src.split('.')[-1]
            copied_src = f'{self.physical_storage}/{_hash}.{_type}.{suffix}'
            shutil.copy(_src, copied_src)
            self.resources.append((_hash, _type, copied_src))
            self.log('add', verbose=verbose, _hash=_hash,
                     _type=_type, _src=copied_src)
        self.resources.append((_hash, _type, _src))
        self.log('add', verbose=verbose, _hash=_hash, _type=_type, _src=_src)

    def select(self, _filter=None, requirements=['raw', 'yaml']):
        target = {}
        self.data = {}
        for _hash, _type, _src in self.resources:
            if _hash not in self.data.keys():
                self.data[_hash] = {}
            if exists(_src):
                self.data[_hash][_type] = _src
            else:
                print(f'{_src} not exist.')

        for _hash in self.data.keys():
            for req in requirements:
                if req not in self.data[_hash].keys():
                    break
            else:
                for _type in self.data[_hash].keys():
                    if _filter and (_filter not in self.data[_hash][_type]):
                        break
                else:
                    target[_hash] = self.data[_hash]
                    self.log(f'selected sample {_hash}')
        self.data = target
        return self.data

    def get_class(self, _hash):
        yaml_path = self.data[_hash]['yaml']
        return self.__from_yaml_read_cls(yaml_path)

    def augment(self, _hash):
        raw = self.data[_hash]['raw']
        mask = self.data[_hash]['mask']
        images = [[raw, mask]]
        # images = [[np.asarray(Image.open(x).convert('RGB')) for x in image]
        #           for image in images]
        images = [[np.asarray(Image.open(x)) for x in image]
                  for image in images]

        p = Augmentor.DataPipeline(images)
        p.rotate(1, max_left_rotation=5, max_right_rotation=5)
        p.flip_top_bottom(0.5)
        p.zoom_random(1, percentage_area=0.5)

        augmented_images = p.sample(7)
        return [{'raw': x[0], 'mask': x[1], 'yaml':self.data[_hash]['yaml']} for x in augmented_images]

    def random(self):
        return random.choice([x for x in self.data.keys()])

    def build(self, test=0.1, augmentor=True, width=200, height=150):
        self.clean_build()
        build_path = create_if_not_exists(self.config.build_path)

        cans = {}
        for _hash in self.data.keys():
            _class = self.get_class(_hash)
            if _class not in cans.keys():
                cans[_class] = []
            cans[_class].append(_hash)

        dataset = {
            'train': [],
            'test': [],
        }

        for _class in cans.keys():
            can = cans[_class]
            size = len(can)
            test_size = int(size*test)
            _test = random.sample(can, test_size)
            _train = [x for x in can if x not in _test]
            dataset['test'].extend(_test)
            dataset['train'].extend(_train)

        for f1 in ['train', 'test']:
            set_path = create_if_not_exists(f'{build_path}/{f1}')
            for _hash in dataset[f1]:
                self.log(f'building {_hash}')
                item_path = create_if_not_exists(f'{set_path}/{_hash}')
                for _type in self.data[_hash]:
                    _addr = self.data[_hash][_type]
                    suffix = _addr.split('.')[-1]
                    if _type == 'yaml':
                        shutil.copy(
                            _addr, f'{item_path}/{_type}.yaml'
                        )
                    else:
                        self.resize(
                            _addr, f'{item_path}/{_type}.{suffix}', width, height)
                if augmentor:
                    augmented_images = self.augment(_hash)
                    for index, augmented_image in enumerate(augmented_images):
                        auged_item_path = create_if_not_exists(
                            f'{item_path}_aug{index}')
                        for _type in augmented_image.keys():
                            # for yaml special
                            if _type == 'yaml':
                                shutil.copy(
                                    augmented_image[_type], f'{auged_item_path}/{_type}.yaml')
                            else:
                                _img = Image.fromarray(augmented_image[_type])
                                _tmp_fp = f'{auged_item_path}/{_type}.png'
                                _img.save(_tmp_fp)
                                self.resize(_tmp_fp, _tmp_fp, width, height)
        self.log('build finished', path=build_path)


if __name__ == "__main__":

    # 从数据库名称启动内核
    config = Config()
    k = DatabaseKernel(config)

    # # 直接从总文件夹读取各类数据
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.mask.png', _type='mask', copy=False, verbose=1)
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.raw.png', _type='raw', copy=False, verbose=1)
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.yaml.yaml', _type='yaml', copy=False, verbose=1)

    # # 保存内核状态
    # k.save()

    # # 清空编译结果
    # k.clean_build()

    # # 选择并编译
    k.select(requirements=['raw', 'mask', 'yaml'])
    k.build(augmentor=True)
