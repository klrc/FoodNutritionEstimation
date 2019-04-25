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
