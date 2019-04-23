#%%
import sys
sys.path.append('.')
from detection.workbench import Workbench

#%%
w = Workbench()
w.init_model('inference', path='data/mask_rcnn_shapes_1600.h5')

#%%
# new_img = w.resize('data/qt5.png', '__cache__/qt5_resized.png',
                    # width=200, height=150)
w.detect(w.read_image('__cache__/qt3_resized.png'))
w.detect(w.read_image('__cache__/qt5_resized.png'))
