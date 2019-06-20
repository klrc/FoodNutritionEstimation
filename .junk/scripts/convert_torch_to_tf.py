# %%
# 读取显卡设备信息
import torch
# import torchvision
import sys
sys.path.append('.')

# Assume that we are on a CUDA machine, then this should print a CUDA device:
device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
print(device)

# 载入网络模型
net = torch.load('/home/sh/Github/FoodNutritionEstimation/research/volume/pretrained_model/non_para_model.torch')
# net = torchvision.models.alexnet(num_classes=10).cuda()
# net = torchvision.models.vgg11(num_classes = 10)
# net = torchvision.models.inception_v3(num_classes = 10)
# net = torchvision.models.resnet18(num_classes = 10)
# net = torchvision.models.densenet121(num_classes=10).cuda()


# %%
# 转换模型 pytorch->onnx->tensorflow
from utils.kernel_onnx import Config, OnnxKernel  # noqa: E402
onnx = OnnxKernel(Config())
onnx.from_torch(net)
onnx.to_tensorflow()

# %%
