# %%
from onnx_tf.backend import prepare
import onnx
from collections import OrderedDict
import torchsummary
import sys
import torch
sys.path.append('.')


# %%
from research.map_volume.cells.SIDepth import SENetSIDepth  # noqa:E402
net = SENetSIDepth()
state_dict = torch.load('.downloads/pretrained_model/model_senet.pth')
new_state_dict = OrderedDict()
for k, v in state_dict.items():
    name = k[7:]  # remove module.
    new_state_dict[name] = v
net.load_state_dict(new_state_dict)
net = net.cuda()
torchsummary.summary(net, input_size=(3, 128, 128))

# %%
dummy_input = torch.randn(1, 3, 128, 128, device='cuda')
torch.onnx.export(net, dummy_input, f'.build/SESID.onnx', verbose=True)

# %%
onnx_model = onnx.load(f'.build/SESID.onnx')
tf_rep = prepare(onnx_model)
tf_rep.export_graph(f'.build/SESID.pb')


# %%
