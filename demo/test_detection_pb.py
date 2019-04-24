# %%
import tensorflow as tf
from tensorflow.python.platform import gfile
import sys
sys.path.append('.')
# %%
pb_path = 'data/__cache__/porting/mask_rcnn_shapes_1600.pb'
sess = tf.Session()
with gfile.FastGFile(pb_path, 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    sess.graph.as_default()
    tf.import_graph_def(graph_def, name='')

writer = tf.summary.FileWriter("data/__cache__/tensorboard/", sess.graph)
sess.run(tf.global_variables_initializer())

# %%
import json  # noqa: E402
with open('data/extra_info.json', 'r') as f:
    extra_info = json.load(f)

# %%
import skimage  # noqa: E402

# Load image
img = skimage.io.imread('data/__cache__/detection/__feeder__/test/img/baozi4.png')
img = skimage.transform.resize(img, (128, 128))
# If grayscale. Convert to RGB for consistency.
if img.ndim != 3:
    img = skimage.color.gray2rgb(img)
# If has an alpha channel, remove it for consistency
if img.shape[-1] == 4:
    img = img[..., :3]
print(img.shape)
# import numpy as np  # noqa: E402
# fake_image = np.random.rand(1, 128, 128, 3)

# %%
input_image_1 = sess.graph.get_tensor_by_name('input_image_1:0')
input_anchors_1 = sess.graph.get_tensor_by_name('input_anchors_1:0')
input_image_meta_1 = sess.graph.get_tensor_by_name('input_image_meta_1:0')
feed_dict = {
    input_image_1: [img],
    input_anchors_1: extra_info['anchors'],
    input_image_meta_1: extra_info['image_metas'],
}
x = sess.run('output_4:0', feed_dict=feed_dict)
print(x)
