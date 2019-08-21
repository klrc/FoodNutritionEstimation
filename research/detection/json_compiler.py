

import json
import os

import PIL.Image
import yaml

from labelme import utils
import base64
import sys
sys.path.append('.')


def compile(path, save_dir=None):

    if os.path.isfile(path):
        with open(path, 'r', encoding='gbk') as f:
            data = json.load(f)

    if data['imageData']:
        imageData = data['imageData']
    else:
        imagePath = os.path.join(
            os.path.dirname(path), data['imagePath'])
        with open(imagePath, 'rb') as f:
            imageData = f.read()
            imageData = base64.b64encode(imageData).decode('utf-8')

    img = utils.img_b64_to_arr(imageData)
    label_name_to_value = {'_background_': 0}
    for shape in data['shapes']:
        label_name = shape['label']
        if label_name in label_name_to_value:
            label_value = label_name_to_value[label_name]
        else:
            label_value = len(label_name_to_value)
            label_name_to_value[label_name] = label_value

    # label_values must be dense
    label_values, label_names = [], []
    for ln, lv in sorted(label_name_to_value.items(), key=lambda x: x[1]):
        label_values.append(lv)
        label_names.append(ln)
    assert label_values == list(range(len(label_values)))
    lbl = utils.shapes_to_label(
        img.shape, data['shapes'], label_name_to_value)
    info = dict(label_names=label_names)

    if save_dir:
        if not os.path.exists(save_dir):
            os.mkdir(save_dir)

        PIL.Image.fromarray(img).save(os.path.join(save_dir, 'img.png'))
        utils.lblsave(os.path.join(save_dir, 'label.png'), lbl)
        with open(os.path.join(save_dir, 'info.yaml'), 'w') as f:
            yaml.safe_dump(info, f, default_flow_style=False)

    return img, lbl, info


if __name__ == "__main__":
    compile('/home/sh/Downloads/师弟标注数据/小排汤/小排汤_56.json', 'test')
