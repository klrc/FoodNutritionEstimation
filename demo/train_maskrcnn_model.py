# %%
import sys
import os
sys.path.append('.')
os.environ["CUDA_VISIBLE_DEVICES"] = "0"


# %%
from research.detection.kernel import Kernel  # noqa: E402


# %%
# 训练模型
# all/heads：训练整个网络/网络尾部 详见declaration
# epoch：目标迭代轮数，注意不是训练多少轮

# k = Kernel()
# k.build('training')
# # k.load_weights(special='coco')
# k.load_weights('data/__cache__/detection/__logs__/' +
#                'detection20190509T1729/mask_rcnn_detection_1600.h5')
# # k.train(epoch=2400, layers='all')
# k.train(epoch=3000, learning_rate_coefficient=1, layers='heads')

# %%
# 读取训练模型并评估

import matplotlib.pyplot as plt  # noqa： E402
import numpy as np  # noqa： E402

k = Kernel()
k.build('inference')
prefix = 'data/__cache__/detection/__logs__/detection20190509T1729'

x = np.arange(1000, 3001)
x = [x_i for x_i in x if x_i % 10 == 0]

y = []
for i in x:
    mid = str(i).zfill(4)
    k.load_weights(path=f'{prefix}/mask_rcnn_detection_{mid}.h5')
    y.append(k.eval())
plt.plot(x, y)

plt.savefig("train_res_1000_3000.png")
# 下面行不写会出现 [<matplotlib.lines.Line2D at 0x111814390>] 字样
plt.show()

# %%
# 测试单张图片
# k = Kernel()
# k.build('inference')
# k.load_weights('data/__cache__/detection/__logs__/' +
#                'detection20190509T1729/mask_rcnn_detection_0830.h5')

# img_path = 'data/__cache__/detection/__feeder__/test/img/baozi4.png'
# img_raw = k.read_image(img_path)
# k.detect(img_raw)
