# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
k = Kernel()
k.build('inference')
k.load_weights('data/mask_rcnn_shapes_1600.h5')

# %%
from research.porting.keras2tf import keras_to_tensorflow  # noqa: E402
keras_to_tensorflow(k.model.keras_model, output_dir='data/__cache__/porting',
                    model_name="mask_rcnn_shapes_1600.pb")
print("MODEL SAVED")
