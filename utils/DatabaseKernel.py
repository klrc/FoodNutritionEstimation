
import os
import json
import shutil
import datetime
import random
import yaml
import numpy as np
from PIL import Image
import Augmentor
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True


class KernelEnv():

    def __init__(self, frozen_core=None, storage_dir=None):
        self.logs = []
        self.resources = []
        self.data = {}
        self.physical_storage = None

        self.frozen_core = frozen_core
        if frozen_core:
            try:
                self.load(frozen_core)
                if storage_dir:
                    self.physical_storage = self.create_physical_storage(
                        storage_dir)
                else:
                    self.log('physical storage', self.physical_storage)
            except:  # noqa: E722
                self.log('failed to load existing archive.', frozen_core)
        else:
            storage_dir = storage_dir if storage_dir else '.'
            self.physical_storage = self.create_physical_storage(storage_dir)
        self.log('Kernel online', 'ready for request.')

    def reset(self):
        self.logs = []
        self.resources = []
        self.data = {}
        self.log('Kernel reset', 'success.')

    def clean(self):
        self.data = {}
        self.log('kernel sources cleaned', 'self.data')
        self.clean_build()

    def clean_build(self):
        build_path = f'{self._get_dir(self.physical_storage)}/build'
        if os.path.exists(build_path):
            shutil.rmtree(build_path)
        self.log('kernel builds cleaned', build_path)

    def create_build(self):
        root = self._get_dir(self.physical_storage)
        self.create_dir(root, 'build')
        return f'{self._get_dir(self.physical_storage)}/build'

    def create_dir(self, root=None, _dir=None, path=None):
        if not path:
            path = f'{root}/{_dir}'
        if not os.path.exists(path):
            os.makedirs(path)
        self.log('build dir:', path)
        return path

    def clean_resources(self):
        raw_len = len(self.resources)
        self.resources = list(set(self.resources))
        new_len = len(self.resources)
        self.log('clean resources', f'{raw_len-new_len} duplicates.')

    def load(self, path):
        # 载入db核心
        with open(path, 'r') as f:
            frozen = json.load(f)
            self.__recover_from(frozen)
            self.log('Existing archive loaded from', path)

    def __recover_from(self, frozen):
        self.logs = frozen['logs']
        self.resources = [tuple(x) for x in frozen['resources']]
        self.data = frozen['data']
        self.physical_storage = frozen['physical_storage']

    def save(self, path=None):
        # 保存db核心
        if not path:
            path = self.frozen_core
        if not path:
            path = './auto_save.frozen'
        save_dir = self._get_dir(path)
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        with open(path, 'w') as f:
            self.log('.frozen saved at', path)
            json.dump(self.freeze(), f)
        self.save_license()

    def freeze(self):
        frozen = {
            'logs': self.logs,
            'resources': self.resources,
            'data': self.data,
            'physical_storage': self.physical_storage,
        }
        return frozen

    def create_physical_storage(self, path):
        if not os.path.exists(path):
            os.makedirs(path)
        self.log('physical storage', path)
        return path

    def log(self, message, target, verbose=1):
        # 记录事务日志
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        msg = f'[{t}] {message}: {target}'
        if verbose > 0:
            print(msg)
        self.logs.append(msg)

    def _get_dir(self, path):
        return self.__split_fp(path)[0]

    def _get_file(self, path):
        return self.__split_fp(path)[1]

    def __split_fp(self, path):
        fp_dir = '/'.join(path.split('/')[0:-1])
        fp_file = path.split('/')[-1]
        return fp_dir, fp_file

    def save_license(self):
        import time
        now = int(time.time())
        path = f'{self._get_dir(self.physical_storage)}/time.license'
        with open(path, 'w') as f:
            f.write(str(now))


class DatabaseKernel(KernelEnv):

    def __init__(self, frozen_core=None, storage_dir=None):
        super().__init__(frozen_core=frozen_core, storage_dir=storage_dir)
        self.log('Kernel initialized', 'type: Database Kernel.')

    def __from_yaml_read_cls(self, yaml_path):
        with open(yaml_path) as f:
            temp = yaml.load(f.read(), Loader=yaml.FullLoader)
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
        self.log('Scanning started at', path)
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
        self.log('Scanning finished with',
                 f'{counter} results. (pattern={pattern})')

    def auto_scan(self, path, pattern, _filter=None, _type='auto', recursive=True, copy=True, verbose=0):
        for _hash, _type, _src in self.scan(path, pattern, _filter, _type, recursive):
            self.add(_hash, _type, _src, copy=copy, verbose=verbose)
        self.clean_resources()

    def add(self, _hash, _type, _src, copy=False, verbose=1):
        if copy:
            suffix = _src.split('.')[-1]
            copied_src = f'{self.physical_storage}/{_hash}.{_type}.{suffix}'
            shutil.copy(_src, copied_src)
            self.resources.append((_hash, _type, copied_src))
            self.log('Add rec', (_hash, _type, copied_src), verbose=verbose)
        self.resources.append((_hash, _type, _src))
        self.log('Add rec', (_hash, _type, _src), verbose=verbose)

    def select(self, _filter=None, requirements=['raw', 'yaml']):
        target = {}
        self.data = {}
        for _hash, _type, _src in self.resources:
            if _hash not in self.data.keys():
                self.data[_hash] = {}
            if os.path.exists(_src):
                self.data[_hash][_type] = _src

        for _hash in self.data.keys():
            for req in requirements:
                if req not in self.data[_hash].keys():
                    print(f'denied {_hash} {req} / {[x for x in self.data[_hash].keys()]}')
                    break
            else:
                for _type in self.data[_hash].keys():
                    if _filter and (_filter not in self.data[_hash][_type]):
                        break
                else:
                    target[_hash] = self.data[_hash]
                    self.log('selected sample', f'{_hash}.')
        self.data = target
        return self.data

    def get_class(self, _hash):
        yaml_path = self.data[_hash]['yaml']
        return self.__from_yaml_read_cls(yaml_path)

    def augment(self, _hash):
        raw = self.data[_hash]['raw']
        mask = self.data[_hash]['mask']
        images = [[raw, mask]]
        images = [[np.asarray(Image.open(x).convert('RGB')) for x in image]
                  for image in images]

        p = Augmentor.DataPipeline(images)
        p.rotate(1, max_left_rotation=5, max_right_rotation=5)
        p.flip_top_bottom(0.5)
        p.zoom_random(1, percentage_area=0.5)

        augmented_images = p.sample(3)
        return [{'raw': x[0], 'mask': x[1], 'yaml':self.data[_hash]['yaml']} for x in augmented_images]

    def random(self):
        return random.choice([x for x in self.data.keys()])

    def build(self, test=0.1, augmentor=True, width=200, height=150):
        self.clean_build()
        build_path = self.create_build()

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
            set_path = self.create_dir(build_path, f1)
            for _hash in dataset[f1]:
                item_path = self.create_dir(set_path, _hash)
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
                        auged_item_path = f'{item_path}_aug{index}'
                        self.create_dir(path=auged_item_path)
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
        self.log('build finished at', build_path)


if __name__ == "__main__":

    # 从数据库名称启动内核
    dbname = 'FoodMask60'
    k = DatabaseKernel(frozen_core=f'data/{dbname}/{dbname}.frozen',
                       storage_dir=f'data/{dbname}/storage')

    # # 直接从总文件夹读取各类数据
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.mask.png', _type='mask', copy=False, verbose=1)
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.raw.png', _type='raw', copy=False, verbose=1)
    # k.auto_scan(path='data/FoodMask60/storage', pattern='.yaml.yaml', _type='yaml', copy=False, verbose=1)

    # # 保存内核状态
    # k.save()

    # 清空编译结果
    k.clean_build()

    # 选择并编译
    k.select(requirements=['raw', 'mask', 'yaml'])
    k.build(augmentor=False)
