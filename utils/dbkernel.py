import os
import json
import shutil
import datetime


class Kernel:

    def __init__(self, frozen_core=None, storage_path='.'):
        self.logs = []
        self.resources = []
        self.data = []
        self.physical_storage = self.create_physical_storage(storage_path)

        self.frozen_core = frozen_core
        if frozen_core:
            try:
                self.load(frozen_core)
            except:  # noqa: E722
                self.log('failed to load existing archive.', frozen_core)
        self.log('Kernel online', 'ready for request.')

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
            self.clean_kernel()
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

    # def build():
    #     pass

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


# k = Kernel('test/core.db')
# k.add('tmp_resized', {'path': 'data/__cache__/tmp_resized.png'}, copy=True)
# k.remove('tmp_resize')
# k.remove('tmp_resized')
k = Kernel(frozen_core='test/auto.frozen', storage_path='test/storage')
k.auto_scan('data', copy=False, check_flag='yaml')
k.save()
