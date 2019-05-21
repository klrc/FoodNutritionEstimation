import os
import json
import shutil
import datetime
import random


class Kernel:

    def __init__(self, frozen_core=None, storage_dir=None):
        self.logs = []
        self.resources = []
        self.data = []
        self.physical_storage = None

        self.frozen_core = frozen_core
        if frozen_core:
            try:
                self.load(frozen_core)
                if storage_dir:
                    self.physical_storage = self.create_physical_storage(storage_dir)
                else:
                    self.log('physical storage', self.physical_storage)
            except:  # noqa: E722
                self.log('failed to load existing archive.', frozen_core)
        else:
            storage_dir = storage_dir if storage_dir else '.'
            self.physical_storage = self.create_physical_storage(storage_dir)
        self.log('Kernel online', 'ready for request.')

    def build(self, shuffle=True, horizontal_split=True, augmentor=True):
        pass

    def clear_data(self):
        self.data = []

    def select(self, num=None, requirements=['png', 'yaml']):
        table = {}
        for x in self.resources:
            if x[0] not in table.keys():
                table[x[0]] = {}
            table[x[0]][x[1]] = x[2]
        for x in table.keys():
            for req in requirements:
                if req not in table[x].keys():
                    break
            else:
                if x not in [t[0] for t in self.data]:
                    self.data.append((x, table[x]))
        if num:
            num = len(self.data) if num > len(self.data) else num
            self.data = random.sample(self.data, num)
        self.log('Selected samples',
                 f'{len(self.data)} items of {len(table.keys())} selected.')

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
        self.log('Add rec', xpath, verbose=verbose)

    def auto_scan(self, path, recursive=True, copy=True, check_flag=None, verbose=0):
        for x in self.scan(path, recursive=recursive, check_flag=check_flag):
            path, name = x
            xhash = name.split('.')[0]
            xtype = name.split('.')[1]
            xpath = f'{path}/{name}'
            self.add(xhash, xtype, xpath, copy=copy, verbose=verbose)
        self.clean_kernel()

    def scan(self, path, recursive=True, check_flag=None):
        self.log('Scanning started at', path)
        counter = 0
        if recursive:
            for x in os.walk(path):
                if len(x[2]) > 0:
                    for item in x[2]:
                        if self.check(item, check_flag):
                            counter += 1
                            yield(x[0], item)
        else:
            for x in os.listdir(path):
                if self.check(x, check_flag):
                    counter += 1
                    yield (path, x)
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


k = Kernel(frozen_core='data/db/core.frozen')
# k.auto_scan('data/__cache__/', copy=True, check_flag='yaml', verbose=1)
# k.auto_scan('data/__cache__/', copy=True, check_flag='png', verbose=1)
k.select(400)
k.save()
