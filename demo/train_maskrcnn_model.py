# %%
import sys
import os
import tensorflow as tf
from keras.backend.tensorflow_backend import set_session
sys.path.append('.')
os.environ["CUDA_VISIBLE_DEVICES"] = "0"
config = tf.ConfigProto()
config.gpu_options.per_process_gpu_memory_fraction = 0.9
set_session(tf.Session(config=config))

# %%
from research.detection.kernel import Kernel  # noqa: E402


# %%
# 训练模型
# all/heads：训练整个网络/网络尾部 详见declaration
# epoch：目标迭代轮数，注意不是训练多少轮

k = Kernel()
k.build('training')
k.load_weights(special='coco')
# k.load_weights(special='last')
# k.load_weights('data/__cache__/detection/__logs__/' +
#    'detection20190509T1729/mask_rcnn_detection_3000.h5')
k.train(epoch=400, layers='all')
# # k.train(epoch=3000, learning_rate_coefficient=1, layers='heads')

# %%
# 读取训练模型并评估

# import matplotlib.pyplot as plt  # noqa： E402
# import numpy as np  # noqa： E402

# k = Kernel()
# k.build('inference')
# prefix = 'data/__cache__/detection/__logs__/detection20190509T1729'

# x = np.arange(3000, 4500)
# x = [x_i for x_i in x if x_i % 10 == 0]

# y = []
# for i in x:
#     mid = str(i).zfill(4)
#     k.load_weights(path=f'{prefix}/mask_rcnn_detection_{mid}.h5')
#     y.append(k.eval())
# plt.plot(x, y)

# plt.savefig("docs/train_res_1000_4500.png")
# # 下面行不写会出现 [<matplotlib.lines.Line2D at 0x111814390>] 字样
# plt.show()

# %%
# 测试单张图片
k = Kernel()
k.build('inference')

'''
    (no augmentation)
    LOSS_WEIGHTS = (default)
    learning_rate_coefficient = 1 (default)
'''
detection_control_sample_0 = '/home/sh/github/FoodNutritionEstimation/data/__cache__/detection/logs/detection20190612T1906/mask_rcnn_detection_0400.h5'

'''
    LOSS_WEIGHTS = {
        "rpn_class_loss": 0.8,
        "rpn_bbox_loss": 0.8,
        "mrcnn_class_loss": 1.,
        "mrcnn_bbox_loss": 0.8,
        "mrcnn_mask_loss": 0.8
    }
    learning_rate_coefficient = 1 (default)
'''
detection_control_sample_1 = '/home/sh/github/FoodNutritionEstimation/data/__cache__/detection/logs/detection20190613T1748/mask_rcnn_detection_0400.h5'


test_list = [
    detection_control_sample_0,
    detection_control_sample_1,
]

img_samples = [k.random_img() for x in range(8)]
k.display_images(img_samples, cols=8)

# %%
# 绘制图表
import matplotlib.pyplot as plt  # noqa: E402
_, axes = plt.subplots(2, 8, figsize=(128, 32))

for cor_x, sample in enumerate(test_list):
    print(f'INF\t{sample}')
    k.load_weights(sample)
    for cor_y, img in enumerate(img_samples):
        img, r = k.detect(img, display=False)
        ax, classes = k.visualizeDrawResultOnAx(axes[cor_x, cor_y], img, r)
        print(f'cls\t[{cor_x}, {cor_y}]\t{classes}')

plt.show()

# %%
