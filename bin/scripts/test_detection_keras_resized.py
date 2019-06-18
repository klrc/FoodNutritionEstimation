

# %%
'''
    确认了keras原模型对于resize操作的处理方式
    200×150的原图，在模型强制转化为128×128的情况下，

    resize / 模型内部方法效果差距明显
    后发现模型内部的方法并不是resize，而是等比缩放后补边

    存在过拟合的隐患，但是没有其他证据证明
'''

# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
k = Kernel()
k.build('inference')
k.load_weights('mask_rcnn_detection_0830.h5')

img_path = 'F:/GitHub-Package/FoodNutritionEstimation/data/__cache__/detection/__feeder__/test/img/chiken3.png'
img_raw = k.read_image(img_path)
resized_path = 'data/__cache__/tmp_resized.png'
k.resize_image(img_path, resized_path, width=128, height=128)
img_128 = k.read_image(resized_path)

# %%
k.detect(img_raw)
k.detect(img_128)

# %%
molded_images, image_metas, anchors = k.get_extra_infos([img_raw])
print(molded_images.shape)
k.detect(molded_images[0])
