# %%
import json
from tensorflow.python.platform import gfile
import tensorflow as tf
import os
from keras import backend
import sys
import numpy as np
sys.path.append('.')

model_id = 'MRCNND_p10_ld003_e1000_lyAsrF_20190827162647_1000.h5'
checkpoint_path = '.build/map_detection/checkpoints'

# %%
'''
    freeze a network as inference mode
'''

from research.map_detection.cells.network import MaskRCNN  # noqa: E402

network = MaskRCNN.compile('inference')
if f'inf_{model_id}' in os.listdir('.build'):
    network.model.load_weights(f'.build/inf_{model_id}')
else:
    network.model.load_weights(f'{checkpoint_path}/{model_id}', by_name=True)
    network.model.keras_model.save(
        f'.build/inf_{model_id}', include_optimizer=False)

network.model.keras_model.summary()


# %%
'''
    # MRCNN structure:
            # Detections
            # output is [batch, num_detections, (y1, x1, y2, x2, class_id, score)] in
            # normalized coordinates
            detections = DetectionLayer(config, name="mrcnn_detection")(
                [rpn_rois, mrcnn_class, mrcnn_bbox, input_image_meta])

            # Create masks for detections
            detection_boxes = KL.Lambda(lambda x: x[..., :4])(detections)
            mrcnn_mask = build_fpn_mask_graph(detection_boxes, mrcnn_feature_maps,
                                              input_image_meta,
                                              config.MASK_POOL_SIZE,
                                              config.NUM_CLASSES,
                                              train_bn=config.TRAIN_BN)

            model = KM.Model([input_image, input_image_meta, input_anchors],
                             [detections, mrcnn_class, mrcnn_bbox,
                                 mrcnn_mask, rpn_rois, rpn_class, rpn_bbox],
                             name='mask_rcnn')
'''

# %%
# *-coding:utf-8-*

"""
将keras的.h5的模型文件，转换成TensorFlow的pb文件
"""


def h5_to_pb(h5_model, output_dir, model_name, out_prefix="output_", log_tensorboard=False):
    """.h5模型文件转换成pb模型文件
    Argument:
        h5_model: str
            .h5模型文件
        output_dir: str
            pb模型文件保存路径
        model_name: str
            pb模型文件名称
        out_prefix: str
            根据训练，需要修改
        log_tensorboard: bool
            是否生成日志文件
    Return:
        pb模型文件
    """
    if os.path.exists(output_dir) is False:
        os.mkdir(output_dir)
    out_nodes = []
    for i in range(len(h5_model.outputs)):
        out_nodes.append(out_prefix + str(i + 1))
        tf.identity(h5_model.output[i], out_prefix + str(i + 1))
    sess = backend.get_session()

    from tensorflow.python.framework import graph_util, graph_io
    # 写入pb模型文件
    init_graph = sess.graph.as_graph_def()
    main_graph = graph_util.convert_variables_to_constants(
        sess, init_graph, out_nodes)
    graph_io.write_graph(main_graph, output_dir,
                         name=model_name, as_text=False)
    # 输出日志文件
    if log_tensorboard:
        from tensorflow.python.tools import import_pb_to_tensorboard
        import_pb_to_tensorboard.import_to_tensorboard(
            os.path.join(output_dir, model_name), output_dir)


#  .h模型文件路径参数
input_path = '.build'
weight_file = 'inf_MRCNND_p10_ld003_e1000_lyAsrF_20190827162647_1000.h5'
weight_file_path = os.path.join(input_path, weight_file)
output_graph_name = weight_file[:-3] + '.pb'

#  pb模型文件输出输出路径
output_dir = '.build'

if output_graph_name not in os.listdir(output_dir):
    #  convertion
    h5_to_pb(network.model.keras_model, output_dir=output_dir,
             model_name=output_graph_name)
    print('.h5 -> .pb convertion finished.')


# %%
'''
    freeze metas
'''


def read_image(path):
    """Load the specified image and return a [H,W,3] Numpy array.
    """
    import skimage
    # Load image
    image = skimage.io.imread(path)
    # If grayscale. Convert to RGB for consistency.
    if image.ndim != 3:
        image = skimage.color.gray2rgb(image)
    # If has an alpha channel, remove it for consistency
    if image.shape[-1] == 4:
        image = image[..., :3]
    return image


metas_dir = '.build/metas.pak'
if not os.path.exists(metas_dir):
    os.makedirs(metas_dir)
    image_paths = [
        '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-46743409478703622/img.png',
        # '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-46614495560621320/img.png'
    ]
    images = [read_image(image_path) for image_path in image_paths]
    # Mold inputs to format expected by the neural network
    molded_images, image_metas, windows = network.model.mold_inputs(images)
    image_shape = molded_images[0].shape
    for g in molded_images[1:]:
        assert g.shape == image_shape,\
            "After resizing, all images must have the same size. Check IMAGE_RESIZE_MODE and image sizes."
    # Anchors
    anchors = network.model.get_anchors(image_shape)
    # Duplicate across the batch dimension because Keras requires it
    anchors = np.broadcast_to(
        anchors, (network.model.config.BATCH_SIZE,) + anchors.shape)
    shape_encodes = f'{image_shape[0]}x{image_shape[1]}x{image_shape[2]}'
    with open(f'{metas_dir}/image_metas_{shape_encodes}.json', 'w') as f:
        json.dump({'image_metas': image_metas.tolist(),
                   'anchors': anchors.tolist()}, f)
    print(f'metas:\n{image_metas}\nanchors:\n{anchors}')

# %%
'''
    test tensorflow .pb
'''
pb_path = f'{output_dir}/{weight_file}'

with tf.Session() as sess:
    # 读取得到的pb文件加载模型
    with gfile.FastGFile(pb_path, 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())
        # 把图加到session中
        tf.import_graph_def(graph_def, name='')
    # 获取当前计算图
    graph = tf.get_default_graph()

    img = read_image(
        '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-46743409478703622/img.png')
    with open('.build/metas.pak/image_metas_128x128x3.json', 'r') as f:
        image_metas = json.load(f)

    input_image = sess.graph.get_tensor_by_name('input_image:0')
    input_anchors = sess.graph.get_tensor_by_name('input_anchors:0')
    input_image_meta = sess.graph.get_tensor_by_name('input_image_meta:0')

    feed_dict = {
        input_image: [img],
        input_anchors: image_metas['anchors'],
        input_image_meta: image_metas['image_metas'],
    }
    pred = sess.run('output_1:0', feed_dict=feed_dict)
    print(pred)
