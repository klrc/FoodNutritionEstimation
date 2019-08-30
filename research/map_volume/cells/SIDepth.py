import torch
import torch.nn as nn
from . import modules
from . import SENet


class SIDepth(nn.Module):
    def __init__(self, Encoder, num_features, block_channel):

        super(SIDepth, self).__init__()

        self.E = Encoder
        self.D = modules.D(num_features)
        self.MFF = modules.MFF(block_channel)
        self.R = modules.R(block_channel)

    def forward(self, x):
        x_block1, x_block2, x_block3, x_block4 = self.E(x)
        x_decoder = self.D(x_block1, x_block2, x_block3, x_block4)
        x_mff = self.MFF(x_block1, x_block2, x_block3, x_block4, [
                         x_decoder.size(2), x_decoder.size(3)])
        out = self.R(torch.cat((x_decoder, x_mff), 1))

        return out


class SENetSIDepth(SIDepth):
    def __init__(self):
        original_model = SENet.senet154(
            pretrained='imagenet', model_dir='.downloads/pretrained_model/encoder')
        Encoder = modules.E_senet(original_model)

        super().__init__(Encoder, num_features=2048,
                         block_channel=[256, 512, 1024, 2048])
