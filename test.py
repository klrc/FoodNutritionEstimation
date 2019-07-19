<<<<<<< HEAD
from bin.FoodMask60.kernel_database import Config, DatabaseKernel

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
=======
import torch
# import collections
import keras

def main():    
    print('tttt')

if __name__ == '__main__':    
    main()
>>>>>>> refs/remotes/origin/master
