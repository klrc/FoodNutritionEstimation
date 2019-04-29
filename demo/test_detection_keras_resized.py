
# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
k = Kernel()
k.build('inference')
k.load_weights('data/mask_rcnn_shapes_1600.h5')

img_path = 'data/__cache__/detection/__feeder__/test/img/baozi4.png'
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