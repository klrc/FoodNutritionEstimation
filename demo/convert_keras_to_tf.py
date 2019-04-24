# %%
import sys
sys.path.append('.')
from detection.workbench import Workbench

# %%
w = Workbench()
w.init_model('inference', path='data/mask_rcnn_shapes_1600.h5')

# %%
from porting.keras2tf import keras_to_tensorflow
keras_to_tensorflow(w.model.keras_model, output_dir='__cache__',
                    model_name="mask_rcnn_shapes_1600.pb")
print("MODEL SAVED")
