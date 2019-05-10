# %%
import sys
import os 
sys.path.append('.')
os.environ["CUDA_VISIBLE_DEVICES"] = "0"


# %%
from research.detection.kernel import Kernel  # noqa: E402


# %%
k = Kernel()
k.build('training')
# k.load_weights(special='coco')
# k.train(epoch=800, learning_rate_coefficient=1, layers='heads')
k.load_weights('data/__cache__/detection/__logs__/' +
               'detection20190509T1729/mask_rcnn_detection_0800.h5')
k.train(epoch=40, layers='all')

# %%
# k = Kernel()
# k.build('inference')
# prefix = 'data/__cache__/detection/__logs__/detection20190509T1729'
# for i in range(80):
#     mid = str(10*(i+1)).zfill(4)
#     k.load_weights(path=f'{prefix}/mask_rcnn_detection_{mid}.h5')
#     print(k.eval())

# %%
