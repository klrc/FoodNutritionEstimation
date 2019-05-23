import os
import json
import shutil
import datetime
import random
import yaml


class Kernel:

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

    def from_yaml_read_cls(self, yaml_path):
        with open(yaml_path) as f:
            temp = yaml.load(f.read(), Loader=yaml.FullLoader)
            labels = temp['label_names']
            del labels[0]
        return labels[0]

    def build(self, test=0.1, horizontal_split=True, augmentor=True, num=None):
        if len(self.data.keys()) == 0:
            self.select(requirements=['png', 'yaml'])
        if horizontal_split:
            cls_container = {}
            for h in self.data.keys():
                h_cls = self.from_yaml_read_cls(self.data[h]['yaml'])
                if h_cls not in cls_container.keys():
                    cls_container[h_cls] = []
                cls_container[h_cls].append(h)
            test_set = []
            train_set = []
            for cls in cls_container.keys():
                size = len(cls_container[cls])
                test_size = int(size*test)
                test_sub_set = random.sample(cls_container[cls], test_size)
                train_sub_set = [x for x in cls_container[cls]
                                 if x not in test_sub_set]
                test_set.extend(test_sub_set)
                train_set.extend(train_sub_set)
                print(cls, len(train_sub_set), len(test_sub_set), size)
            build_path = f'{self.__get_dir(self.physical_storage)}/build'
            if not os.path.exists(build_path):
                os.makedirs(build_path)
            for f1 in ['train', 'test']:
                for f2 in ['img', 'mask', 'yaml']:
                    sub_build_path = f'{build_path}/{f1}/{f2}'
                    if not os.path.exists(sub_build_path):
                        os.makedirs(sub_build_path)
            for h in test_set:
                print(h)

    def clean(self):
        self.data = {}
        self.log('kernel sources cleaned', 'self.data')

        build_path = f'{self.__get_dir(self.physical_storage)}/build'
        if os.path.exists(build_path):
            os.removedirs(build_path)
        self.log('kernel sources cleaned', build_path)

    def select(self, num=None, requirements=['png', 'yaml']):
        table = {}
        for x in self.resources:
            if x[0] not in table.keys():
                table[x[0]] = {}
            table[x[0]][x[1]] = x[2]
        pre_targets = [x for x in self.data.keys()]
        for x in table.keys():
            for req in requirements:
                if req not in table[x].keys():
                    break
            else:
                if x not in pre_targets:
                    pre_targets.append(x)
        if num:
            num = len(pre_targets) if num > len(pre_targets) else num
            pre_targets = random.sample(pre_targets, num)

        for x in pre_targets:
            if x in table.keys():
                self.data[x] = table[x]

        self.log('Selected samples',
                 f'{len(self.data.keys())} items of {len(table.keys())} selected.')

    def create_cache(self):
        storage = self.physical_storage
        if not os.path.exists(f'{storage}/.cache'):
            os.makedirs(f'{storage}/.cache')
        self.log('using cache', f'{storage}/.cache')
        return f'{storage}/.cache'

    def clear_cache(self):
        os.removedirs(f'{self.physical_storage}/.cache')
        self.log('Cleared cache at', f'{self.physical_storage}/.cache')

    def clean_kernel(self):
        raw_len = len(self.resources)
        self.resources = list(set(self.resources))
        new_len = len(self.resources)
        self.log('clean kernel', f'{raw_len-new_len} duplicates.')

    def add(self, xhash, xtype, xpath,  copy=False, verbose=1):
        # 添加索引
        # copy: 同时复制备份到 self.physical_storage 目录下
        if copy:
            target_path = f'{self.physical_storage}/{xhash}.{xtype}'
            shutil.copy(xpath, target_path)
            self.resources.append((xhash, xtype, target_path))
        else:
            self.resources.append((xhash, xtype, xpath))
        self.log('Add rec', (xhash, xtype, xpath), verbose=verbose)

    def auto_scan(self, path, recursive=True, copy=True, check_flag=None, verbose=0):
        for full_path, name in self.scan(path, recursive=recursive, check_flag=check_flag):
            xhash = name.split('.')[0]
            xtype = name.split('.')[1]
            xpath = full_path
            self.add(xhash, xtype, xpath, copy=copy, verbose=verbose)
        self.clean_kernel()

    def scan(self, path, recursive=True, check_flag=None):
        self.log('Scanning started at', path)
        counter = 0
        if recursive:
            for x in os.walk(path):
                if len(x[2]) > 0:
                    for item in x[2]:
                        full_path = f'{x[0]}/{item}'
                        if self.check(full_path, check_flag):
                            counter += 1
                            suffix = check_flag.split('.')[-1]
                            name = self.__get_file(full_path.replace(
                                check_flag, '')) + '.' + suffix
                            yield (full_path, name)
        else:
            for x in os.listdir(path):
                full_path = f'{path}/{x}'
                if self.check(full_path, check_flag):
                    counter += 1
                    suffix = check_flag.split('.')[-1]
                    name = self.__get_file(full_path.replace(
                        check_flag, '')) + '.' + suffix
                    yield (full_path, name)
        self.log('Scanning finished with',
                 f'{counter} results. (check_flag={check_flag})')

    @ staticmethod
    def check(x, flag):
        if not flag:
            return True
        if str(x).endswith(flag):
            return True
        return False

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
        save_dir = self.__get_dir(path)
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        with open(path, 'w') as f:
            self.log('.frozen saved at', path)
            json.dump(self.freeze(), f)

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

    def __get_dir(self, path):
        return self.__split_fp(path)[0]

    def __get_file(self, path):
        return self.__split_fp(path)[1]

    def __split_fp(self, path):
        fp_dir = '/'.join(path.split('/')[0:-1])
        fp_file = path.split('/')[-1]
        return fp_dir, fp_file

    # def seed():
    #     pass

    # def remove(self, hash):
    #     # 删除单个数据索引
    #     if hash in self.db['data'].keys():
    #         self.db['data'].pop(hash)
    #         self.log('remove', hash)

    # def get(self, hash):
    #     # 按hash索引获取数据
    #     if hash in self.db['data'].keys():
    #         return None
    #     return self.db['data'][hash]


# k = Kernel(storage_dir='data/db/storage')
k = Kernel(frozen_core='data/db/core.frozen')
# k.auto_scan('/home/sh/Downloads/netfood2/', copy=True,
#             check_flag='_json/info.yaml', verbose=1)
# k.auto_scan('/home/sh/Downloads/netfood2/', copy=True,
#             check_flag='_json/img.png', verbose=1)
k.select(requirements=['png', 'yaml'])
k.build()
# k.save()
# k.clean()


'''
 TODO:
    inner func / outer
'''
