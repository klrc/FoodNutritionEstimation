# %%
import os
import sys
import json
sys.path.append('.')

# %%
'''
    build records.pak & divides.json
'''
# from cells.compiler import Compiler  # noqa: E402
# size = (200, 150)
# c = Compiler('.build/map_detection')
# pure_json = c.collect('/home/sh/Downloads/数据/')
# records = c.build_records(pure_json, size=(200, 150))
# records = c.augments(records, batch=4)


# %%
'''
    start from records
'''
from cells.compiler import Compiler  # noqa: E402

c = Compiler('.build/map_detection')
base = '.build/map_detection/records.pak'
records = [f'{base}/{x}' for x in os.listdir(base)]
_train, _val = c.build_dataset(records)
with open('.build/map_detection/divides.json', 'w') as f:
    json.dump({'train': _train, 'val': _val}, f)

# %%
'''
    start from frozen datasets
    instantiate train/val
'''
from cells.dataset import FoodMask60  # noqa: E402
size = (200, 150)
with open('.build/detection_map/divides_p30.json', 'r') as f:
    divides = json.load(f)
    _train, _val = divides['train'], divides['val']
print('building datasets ..')
train, val = FoodMask60(_train, size), FoodMask60(_val, size)

# %%
'''
    test batch evolution
'''
from cells.network import MaskRCNN  # noqa: E402
os.environ["CUDA_VISIBLE_DEVICES"] = "2"
evolution_list = [
    '.build/map_detection/config_0.json',
    '.build/map_detection/config_1.json',
    '.build/map_detection/config_2.json'
]

network = MaskRCNN.compile('training')
network.test_evolutions(evolution_list, train, val)

# %%
