# %%
import tensorflow as tf
from tensorflow.python.platform import gfile

# %%
pb_path = 'data/__cache__/porting/mask_rcnn_shapes_1600.pb'
sess = tf.Session()
with gfile.FastGFile(pb_path, 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    sess.graph.as_default()
    tf.import_graph_def(graph_def, name='')
sess.run(tf.global_variables_initializer())

# %%
print(sess.run('output_0:0'))