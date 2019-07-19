# %%
import sys
sys.path.append('.')
from research.detection.FoodMask60 import FoodMask60  # noqa: E402


# %%
ds = FoodMask60(path='data/FoodMask60/build', for_train=False)
ds.display()
print('finish')


# %%
