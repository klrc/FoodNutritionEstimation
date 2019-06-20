# %%
import sys
sys.path.append('.')


# %%
from research.volume.model_def.senet import senet154  # noqa: E402
# from research.volume.model_def.SIDE import SIDE_senet  # noqa: E402
# model = SIDE_senet()
model = senet154(pretrained='imagenet')
model.cuda()

# %%
from utils.kernel_onnx import Config, OnnxKernel  # noqa: E402
onnx = OnnxKernel(Config())
onnx.from_torch(model, name='volinf')
print('onnx built')


#%%
