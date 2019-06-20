# %%
'''
    keras .h5 转为 tensorflow .pb ;
    一切顺利

'''


# %%
import sys
sys.path.append('.')
from research.detection.kernel import Kernel  # noqa: E402

# %%
k = Kernel()
k.build('inference')
k.load_weights(
    '/home/sh/github/FoodNutritionEstimation/data/__cache__/detection/logs/detection20190613T1748/mask_rcnn_detection_0400.h5')

# %%
from research.porting.keras2tf import keras_to_tensorflow  # noqa: E402
keras_to_tensorflow(k.model.keras_model, output_dir='data/Onnx/build',
                    model_name="mask_rcnn_detection_0400.pb")
print("MODEL SAVED")

# %%
