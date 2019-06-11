# %%
import sys
sys.path.append('.')
from research.detection.dataset import FoodDataset  # noqa: E402


# %%
ds = FoodDataset(path='data/FoodMask60/build', for_train=False)
ds.display()
print('finish')


# %%
