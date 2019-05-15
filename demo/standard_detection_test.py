
# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
import os  # noqa: E402
k = Kernel()
k.build('inference')
wdir = 'data/__cache__/detection'
weight_list = os.list_dir(wdir)
weight_list = [f'{wdir}/{x}' for x in weight_list if x.endswith('.h5')]


# %%
test_img = 'data/__cache__/detection/__feeder__/test/img/baozi4.png'
test_img = k.read_image(test_img)

test_img_resized = 'data/__cache__/tmp_resized.png'
k.resize_image(test_img, test_img_resized, width=128, height=128)
test_img_resized = k.read_image(test_img_resized)

molded_images, image_metas, anchors = k.get_extra_infos([test_img])
test_img_molded = molded_images[0]


# %%
for w in weight_list:
    k.load_weights(w)
    k.detect(test_img)
    k.detect(test_img_resized)
    k.detect(test_img_molded)


