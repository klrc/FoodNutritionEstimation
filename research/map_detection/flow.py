# %%
import os
import sys
import json
sys.path.append('.')
from research.map_detection.cells.compiler import Compiler  # noqa: E402


# %%
'''
    map resources check!
'''
compiler_root = '.build/map_detection'
resources = os.listdir(compiler_root)
forced_build = []
resources = [x for x in resources if x not in forced_build]

# %%

'''
    build records.pak
'''

if 'records.pak' not in resources:
    size = (200, 150)
    c = Compiler('.build/map_detection')
    pure_json = c.collect('/home/sh/Downloads/数据/')
    records = c.build_records(pure_json, size=(200, 150))
else:
    base = '.build/map_detection/records.pak'
    records = [f'{base}/{x}' for x in os.listdir(base)]


# %%
'''
    build records_sup.pak
'''

randomise_percentage_area = False
if 'records_sup.pak' not in resources:
    c = Compiler('.build/map_detection')
    records = c.augments(
        records, batch=4, randomise_percentage_area=randomise_percentage_area)
else:
    base = '.build/map_detection/records_sup.pak'
    records_sup = [f'{base}/{x}' for x in os.listdir(base)]
    records.extend(records_sup)


# %%
'''
    build datasets.pak
    start from records
'''
datasets_path = '.build/map_detection/datasets.pak/divides_p30.json'
if 'datasets.pak' not in resources:
    os.makedirs(f'{compiler_root}/datasets.pak')
    c = Compiler('.build/map_detection')
    _train, _val = c.build_dataset(records, val_prop=0.3)
    with open(datasets_path, 'w') as f:
        json.dump({'train': _train, 'val': _val}, f)
else:
    with open(datasets_path, 'r') as f:
        divides = json.load(f)
        _train, _val = divides['train'], divides['val']


# %%
'''
    build datasets
    start from frozen datasets
    instantiate train/val
'''
from research.map_detection.cells.dataset import FoodMask60  # noqa: E402
size = (200, 150)
print('building datasets ..')
train, val = FoodMask60(_train, size), FoodMask60(_val, size)


# %%
'''
    keras configuration
'''
import tensorflow as tf  # noqa:E402
import keras.backend.tensorflow_backend as KTF  # noqa:E402

os.environ["CUDA_VISIBLE_DEVICES"] = "1"
config = tf.ConfigProto()
config.gpu_options.allow_growth = True  # 不全部占满显存, 按需分配
session = tf.Session(config=config)

KTF.set_session(session)


# %%
'''
    reading test configs
    test batch evolution
'''
from research.map_detection.cells.network import MaskRCNN  # noqa: E402
base = '.build/map_detection/configs.pak'

evolution_list = [
    # 'p5_l31_e50_lyHsCrF',
    # 'p5_l32_e50_lyHsCrF',
    # 'p5_l34_e50_lyHsCrF',
    # 'p5_l33_e1000_lyHsCrF',
    'p5_l33_e50_lyAsTrF',
    'p5_l33_e50_lyAsTrF_1',
    'p5_l33_e50_lyAsTrF_2',
]
evolution_list = [f'{base}/{x}.json' for x in evolution_list]

configs = []


def get_config_hash(config):
    '''
        Sample:
            "period": 5,
            "learning_rate": 0.001,
            "epochs": 50,
            "layers": "heads",
            "log_dir": ".log/tensorboard",
            "checkpoint_path": ".build/map_detection/checkpoints/mask_rcnn_detection_{epoch:04d}.h5",
            "coco_path": ".downloads/mask_rcnn_coco.h5",
            "start_from": "coco",
            "randomise_percentage_area": false
    '''
    start_from = config['start_from']
    if start_from == "coco":
        _s = "C"
    else:
        _s = start_from.split('.')[0][-5:-1]

    _p, _l, _e, _ly, _r = config['period'], config['learning_rate'], config['epochs'], config['layers'][0].upper(
    ), 'T' if config['randomise_percentage_area'] else 'F'

    return f"p{_p}_l{str(_l).replace('0.','d')}_e{_e}_ly{_ly}s{_s}r{_r}"


for _path in evolution_list:
    with open(_path, 'r') as f:
        config = json.load(f)
        config['randomise_percentage_area'] = randomise_percentage_area
        config['hash'] = get_config_hash(config)
        configs.append(config)
    with open(_path, 'w') as f:
        json.dump(config, f, indent=4)

network = MaskRCNN.compile('training')
network.test_evolutions(configs, train, val)

# %%
from research.map_detection.cells.network import MaskRCNN  # noqa: E402
network = MaskRCNN.compile('training')
s = network.model.keras_model.summary(line_length=128)

# %%
