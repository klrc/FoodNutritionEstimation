import tensorflow as tf
from tensorflow.python.platform import gfile
import numpy as np
import sys
sys.path.append('.')

# input node:'input.1'
# output node: '2339'

# with tf.Session():
#     print("load graph")
#     with gfile.FastGFile('.build/SESID.pb', 'rb') as f:
#         graph_def = tf.GraphDef()
#         # Note: one of the following two lines work if required libraries are available
#         # text_format.Merge(f.read(), graph_def)
#         graph_def.ParseFromString(f.read())
#         tf.import_graph_def(graph_def, name='')
#         for i, n in enumerate(graph_def.node):
#             # if 'input' in n.name:
#             print("Name of the node - %s" % n.name)

sess = tf.Session()
with gfile.FastGFile(f'.build/SESID.pb', 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    sess.graph.as_default()
    tf.import_graph_def(graph_def, name='')

init = tf.global_variables_initializer()
sess.run(init)

# 1,3,128,128
dummy_input = np.random.rand(1, 3, 64, 64)
ret = sess.run('2339:0', feed_dict={'input.1:0': dummy_input})
print(ret.shape)
