

'''
    终于把这玩意整理成config-run的形式了
    压榨性能的这一天来的太晚了呀

    主要function：

    Kernel.ATTRIBUTE_FOO = bar # 调参
    ...

    k = Kernel() # 类似编译机制
    k.train()
    k.inference()


    后端使用 Keras+Tensorflow

    至于为啥这个核叫Kernel
    因为这样很coooooooooooooooooool

    sad
'''

# %%
import tensorflow as tf
from keras.backend.tensorflow_backend import set_session
import sys
sys.path.append('.')

config = tf.ConfigProto()
config.gpu_options.per_process_gpu_memory_fraction = 0.9
set_session(tf.Session(config=config))


# %%
from bin.Detection.kernel import Kernel  # noqa: E402
Kernel.TRAIN_EPOCHS = 20
Kernel.CHECKPOINT_INTERVAL = 2
c = Kernel()
# c.train()
