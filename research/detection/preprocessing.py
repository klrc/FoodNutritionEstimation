import os.path
import numpy as np
import os
import shutil
from PIL import Image
from PIL import ImageFile

ImageFile.LOAD_TRUNCATED_IMAGES = True


def resize(input_img, output_img, width=128, height=128):
    img = Image.open(input_img)
    try:
        img = img.resize((width, height), Image.BILINEAR)
        img.save(output_img)
        img.close()
    except Exception as e:
        print(e)


def _mkdir(path):
    if not os.path.exists(path):
        os.mkdir(path)


def mkdirs(path):
    _mkdir(path)
    for x in ['train', 'test']:
        _mkdir(f'{path}/{x}')
        _mkdir(f'{path}/{x}/img')
        _mkdir(f'{path}/{x}/mask')
        _mkdir(f'{path}/{x}/yaml')


def scan_data(*dirs):
    for path in dirs:
        foods = os.listdir(path)
        for food in foods:  # 进入菜品文件夹
            if '.' in food:
                continue
            fp = f'{path}/{food}'  # 食物/包子/
            img_folders = os.listdir(fp)  # 包子内所有文件夹和文件  -  列表
            img_folders = [x for x in img_folders if x.endswith('_json')]
            for x in img_folders:
                yield f'{fp}/{x}'


def divide_feeder(data_list):
    np.random.shuffle(data_list)
    _divide_idx = int(len(data_list) / 5)
    return data_list[0:_divide_idx], data_list[_divide_idx:-1]


def gen_feeder(data, output_dir):
    global TOTAL_TASKS, DONE_TASKS
    img_id = data.split('/')[-1].split('_json')[0]
    print(f'processing {img_id} .. ({DONE_TASKS}/{TOTAL_TASKS})')
    DONE_TASKS += 1

    resize(f'{data}/img.png', f'{output_dir}/img/{img_id}.png')
    resize(f'{data}/label.png', f'{output_dir}/mask/{img_id}.png')
    shutil.copyfile(f'{data}/info.yaml', f'{output_dir}/yaml/{img_id}.yaml')


if __name__ == '__main__':
    # feeder_dir = '__feeder__'
    # mkdirs(feeder_dir)
    #
    # data_list = [x for x in scan_data(
    #     '/run/media/sh/My Passport/erkeyiyuan/数据/网络/netfood',
    #     '/run/media/sh/My Passport/erkeyiyuan/数据/本地菜品/压缩包')]
    #
    # TOTAL_TASKS = len(data_list)
    # DONE_TASKS = 0
    #
    # test, train = divide_feeder(data_list)
    #
    # for x in test:
    #     gen_feeder(x, f'{feeder_dir}/test')
    # for x in train:
    #     gen_feeder(x, f'{feeder_dir}/train')

    resize('__feeder__/test/img/baozi4.png', 'testimg.png')
