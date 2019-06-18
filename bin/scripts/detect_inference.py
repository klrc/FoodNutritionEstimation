

# %%
'''
    两张具有相同信息的图片，分别按约100%/60%缩放，
    仅图片的屏占比不同，
    测试结果 mask质量差距很大

    推测由于rpn输出的anchor大小不同，
    导致后续网络的处理精度差距大

    可能需要某种在识别出anchor之后resize的方法？
'''


# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
k = Kernel()
k.build('inference')
k.load_weights('data/mask_rcnn_shapes_1600.h5')

# %%
# new_img = k.resize('data/qt5.png', '__cache__/qt5_resized.png',
# width=200, height=150)
k.detect(k.read_image('data/__cache__/qt3_resized.png'))
k.detect(k.read_image('data/__cache__/qt5_resized.png'))
