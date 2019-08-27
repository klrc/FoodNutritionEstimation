import sys
import argparse
import os

import cv2
import torch
import torch.nn.parallel

from loaddata_demo import readNyu2
from model_def import modules, net, resnet, senet, densenet
from mask import get_mask
from volume import get_volume

os.environ["CUDA_VISIBLE_DEVICES"] = "0"
sys.path.append('.')


parser = argparse.ArgumentParser(description='KD-network')
# parser.add_argument('--img', metavar='DIR', default="./input/test.jpg",
#                     help='img to input')
# parser.add_argument('--json', metavar='DIR', default="./input/test.json",
#                     help='json file to input')
parser.add_argument('--output', metavar='DIR', default="bin/build/volinf",
                    help='dir to output')

args = parser.parse_args()


def define_model(is_resnet, is_densenet, is_senet):
    if is_resnet:
        original_model = resnet.resnet50(pretrained=True)
        Encoder = modules.E_resnet(original_model)
        model = net.model(Encoder, num_features=2048,
                          block_channel=[256, 512, 1024, 2048])
    if is_densenet:
        original_model = densenet.densenet161(pretrained=True)
        Encoder = modules.E_densenet(original_model)
        model = net.model(Encoder, num_features=2208,
                          block_channel=[192, 384, 1056, 2208])
    if is_senet:
        original_model = senet.senet154(pretrained='imagenet')
        Encoder = modules.E_senet(original_model)
        model = net.model(Encoder, num_features=2048,
                          block_channel=[256, 512, 1024, 2048])

    return model


def main():
    if (not os.path.exists(args.output)):
        print("Output directory doesn't exist! Creating...")
        os.makedirs(args.output)

    model = define_model(is_resnet=False, is_densenet=False, is_senet=True).cuda()
    import torchsummary
    torchsummary.summary(model.E, input_size=(3, 128, 128))
#     # model = torch.nn.DataParallel(model).cuda()
#     # model.load_state_dict(torch.load('bin/build/model_senet'))

#     state_dict = torch.load('bin/build/model_senet')
#     # model = torch.load('bin/build/non_para_model.torch')
#     # model = torch.nn.DataParallel(model, device_ids=[0]).cuda()
#     from collections import OrderedDict
#     new_state_dict = OrderedDict()
#     for k, v in state_dict.items():
#         name = k[7:]  # remove module.
#         new_state_dict[name] = v
#     model.load_state_dict(new_state_dict)
#     # torch.save(        original_model = senet.senet154(pretrained='imagenet')
#         Encoder = modules.E_senet(original_model)
#         model = net.model(Encoder, num_features=2048,
#                           block_channel=[256, 512, 1024, 2048])
# model, 'non_para_model.torch')

#     model.cuda()
#     model.eval()
#     # print
#     from utils.kernel_onnx import Config, OnnxKernel  # noqa: E402
#     onnx = OnnxKernel(Config())
#     onnx.from_torch(model, name='volinf')
#     print('onnx built')
#     onnx.to_tensorflow()
#     # test(args.img, model)

#     # test('/home/sh/Github/FoodNutritionEstimation/bin/build/FoodMask60/test/bdcrs7/raw.png', model)


# def test(image, model):
#     img = cv2.imread(image)
#     nyu2_loader = readNyu2(image)
#     width = img.shape[1]
#     height = img.shape[0]
#     for i, image in enumerate(nyu2_loader):
#         image = torch.autograd.Variable(image, volatile=True).cuda()
#         out = model(image)
#         out = out.view(out.size(2), out.size(3)).data.cpu().numpy()
#         max_pix = out.max()
#         min_pix = out.min()
#         out = (out-min_pix)/(max_pix-min_pix)*255
#         out = cv2.resize(out, (width, height), interpolation=cv2.INTER_CUBIC)
#         cv2.imwrite(os.path.join(args.output, "out_grey.png"), out)
#         out_grey = cv2.imread(os.path.join(args.output, "out_grey.png"), 0)
#         out_color = cv2.applyColorMap(out_grey, cv2.COLORMAP_JET)
#         cv2.imwrite(os.path.join(args.output, "out_color.png"), out_color)
#         vol = get_volume(out_grey, args.json)
#         print("Volume:")
#         print(vol)
#         print("unit: cm^3")
#         out_file = open(os.path.join(args.output, "out.txt"), "w")
#         out_file.write("Volume:\n")
#         out_file.write(str(vol))
#         out_file.write("\n")
#         out_file.write("unit: cm^3")
#         out_file.close()
#         get_mask(out_grey, args.json, args.output)


if __name__ == '__main__':
    main()
