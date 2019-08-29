# %%
'''
    basic imports & paths
'''

import keras.backend.tensorflow_backend as KTF
import json
from tensorflow.python.platform import gfile
import tensorflow as tf
import os
from keras import backend
import sys
import numpy as np
sys.path.append('.')


os.environ["CUDA_VISIBLE_DEVICES"] = "0"
config = tf.ConfigProto()
config.gpu_options.allow_growth = True  # 不全部占满显存, 按需分配
session = tf.Session(config=config)
KTF.set_session(session)

model_id = 'MRCNND_p10_ld003_e1000_lyAsrF_20190827162647_1000'
checkpoint_dir = '.build/map_detection/checkpoints'
build_dir = '.build'
inf_model_id = f'inf_{model_id}'


# %%
'''
    load some images for test
'''
from utils.read_image import read_image  # noqa:E402
image_paths = [
    '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-190504432458568228/img.png',
    '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-46743409478703622/img.png',
    '/home/sh/github/FoodNutritionEstimation/.build/map_detection/records.pak/-46614495560621320/img.png'
]
test_images = [read_image(image_path) for image_path in image_paths]

# %%
'''
    freeze a network as inference mode

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

check_keras = False
from research.map_detection.cells.network import MaskRCNN  # noqa: E402
network = MaskRCNN.compile('inference')
network.model.load_weights(f'{checkpoint_dir}/{model_id}.h5', by_name=True)
network.model.keras_model.summary()
if check_keras:
    pred = network.model.detect([test_images[0]])
    if pred:
        print('\ncheck keras model: [OK]')


# %%
"""
    convert keras:.h5 to tensorflow:.pb
"""


def h5_to_pb(h5_model, output_dir, model_name, out_prefix="hubpreds_", log_tensorboard=False):
    if not os.path.exists(output_dir):
        os.mkdir(output_dir)
    out_nodes = []
    for i in range(len(h5_model.outputs)):
        out_nodes.append(out_prefix + str(i + 1))
        print(h5_model.output[i], out_prefix + str(i + 1))
        tf.identity(h5_model.output[i], out_prefix + str(i + 1))
    print()
    keras_sess = backend.get_session()
    from tensorflow.python.framework import graph_util, graph_io
    init_graph = keras_sess.graph.as_graph_def()
    main_graph = graph_util.convert_variables_to_constants(
        keras_sess, init_graph, out_nodes)
    graph_io.write_graph(main_graph, output_dir,
                         name=model_name, as_text=False)
    if log_tensorboard:
        from tensorflow.python.tools import import_pb_to_tensorboard
        import_pb_to_tensorboard.import_to_tensorboard(
            os.path.join(output_dir, model_name), output_dir)


if f'{inf_model_id}.pb' not in os.listdir(build_dir):
    #  convertion
    h5_to_pb(network.model.keras_model, output_dir=build_dir,
             model_name=f'{inf_model_id}.pb', log_tensorboard=True)
    print('.h5 -> .pb convertion finished.')


# %%
'''
    export metas from test image & freeze
'''


metas_dir = '.build/metas.pak'
if not os.path.exists(metas_dir):
    os.makedirs(metas_dir)
    # Mold inputs to format expected by the neural network
    molded_images, image_metas, windows = network.model.mold_inputs([
                                                                    test_images[0]])
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
    with open(f'{metas_dir}/global_metas_{shape_encodes}.json', 'w') as f:
        metas = {'image_metas': image_metas.tolist(),
                 'anchors': anchors.tolist(),
                 'windows': windows.tolist()}
        json.dump(metas, f)
else:
    with open('.build/metas.pak/global_metas_128x128x3.json', 'r') as f:
        metas = json.load(f)
if metas:
    print('load image_metas: [OK]\n')

# %%
'''
    test tensorflow .pb
    with:
        test images + image metas
'''


sess = tf.Session()
with gfile.FastGFile(f'{build_dir}/{inf_model_id}.pb', 'rb') as f:
    graph_def = tf.GraphDef()
    graph_def.ParseFromString(f.read())
    sess.graph.as_default()
    tf.import_graph_def(graph_def, name='')

init = tf.global_variables_initializer()
sess.run(init)

molded_images, image_metas, windows = network.model.mold_inputs([
                                                                test_images[0]])
anchors = metas['anchors']

detections, mrcnn_mask = sess.run(['mrcnn_detection/Reshape_1:0', 'mrcnn_mask/Reshape_1:0'], feed_dict={
    'input_image:0': molded_images,
    'input_anchors:0': anchors,
    'input_image_meta:0': image_metas,
})

# Process detections
results = []
for i, image in enumerate([test_images[0]]):
    final_rois, final_class_ids, final_scores, final_masks =\
        network.model.unmold_detections(detections[i], mrcnn_mask[i],
                                        image.shape, molded_images[i].shape,
                                        windows[i])
    results.append({
        "rois": final_rois,
        "class_ids": final_class_ids,
        "scores": final_scores,
        "masks": final_masks,
    })

print(results)
