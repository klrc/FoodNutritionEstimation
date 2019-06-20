# %%
import sys
sys.path.append('.')


# %%
from research.volume.model_def.SIDE import SIDE_senet  # noqa: E402
model = SIDE_senet()
model.cuda()

# %%
from utils.kernel_onnx import Config, OnnxKernel  # noqa: E402
onnx = OnnxKernel(Config())
onnx.from_torch(model, name='volinf')
print('onnx built')


#%%
