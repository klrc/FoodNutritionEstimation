# Food Nutrition Estimation
[img NA]


test From A Far/
Into the dust/
/

## What does FNE do ? 
患糖尿病的孩子往往需要在各种限制下摄取营养，对于每份食物，都需要严格控制热量、碳水化合物等营养成分。
本项目与儿科医院合作，旨在为糖尿病人家庭估算食物营养提供帮助。

拍摄一张食物照片，模型将自动识别计算它的全部营养信息。



## Demo
[demo NA]



## Definition

原型关注于单张食物照片(image)，构建对食物类别(class)、体积(volume)的感知，输出食物的量化营养成分含量(nutrition)。

* 问题类型：多类别分类问题 + 回归(体积)
  
    31个不同的食物类别：['dumpling', 'tcpg', 'sm', 'qzly', 'potato', 'qchx', 'beefpotato', 'noodles', 'bread', 'mdcsg', 'mdcrs', 'gbrice', 'khs', 'currybeef', 'beef', 'hsyk', 'hstddpg', 'hspg', 'hsjy', 'hsjt', 'chiken', 'hsdy', 'hsdp', 'dtj', 'cyszx', 'cdj', 'crht', 'bdcrs', 'bun', 'bzhx']

* 数据集：医院餐厅标准数据 + 网络食物图像

    [img](NA)

* 标签：所有的图片数据均标注了对应的食物种类以及食物轮廓； 此外， 标准数据还分别包括了31种食物的各个详细成分的[称重测量数据](NA)。

* 细节定义：每张照片(image)包含至少一种食物，食物应当清晰且尽量保证没有中空部分; 食物容器尽量为常见的餐具，以不遮挡食物为佳; 拍摄时尽量调整至俯视视角。

* 应用化：整个算法原型被移植到Android平台，适用于绝大部分设备和应用场景。

    

## General 50K Pipeline Overview
[img]



## Stage1 - Mask R-CNN 食物识别

(origin repo says:)
This is an implementation of [Mask R-CNN](https://arxiv.org/abs/1703.06870) on Python 3, Keras, and TensorFlow. The model generates bounding boxes and segmentation masks for each instance of an object in the image. It's based on Feature Pyramid Network (FPN) and a ResNet101 backbone.

### Getting Started
* [preprocessing.py](research/detection/preprocessing.py) generates the __feeder__ dir from your own dataset, which contains resized images divided into train/test. To have a quick start:
  
        data_list = [x for x in scan_data(
            '/run/media/sh/My Passport/erkeyiyuan/数据/网络/netfood', # change these lines with your own dataset.
            '/run/media/sh/My Passport/erkeyiyuan/数据/本地菜品/压缩包')]

* [core.py](core.py) if the __feeder__ dir is generated, and make sure the path to 'mask_rcnn_coco.h5' is set, you can do whatever you want with core.py.
  
        # Directory to save logs and trained model
        MODEL_DIR = 'detection/__logs__'
    
        # Local path to trained weights file
        COCO_MODEL_PATH = 'mask_rcnn_coco.h5'
        # Download COCO trained weights from Releases if needed
        if not os.path.exists(COCO_MODEL_PATH):
            utils.download_trained_weights(COCO_MODEL_PATH)
    
        # count = len(imglist)  # 文件的数目
        IMG_WIDTH = 200
        IMG_HEIGHT = 150

### Dependencies
### Training
### Evaluating
### Results

## Stage2 - 体积估算
missing intro.
### Dependencies
### Training
### Evaluating
### Results

## Stage3 - 营养成分推断
missing intro.
### Dependencies
### Demo

## Stage4 - 向 Android 移植 
missing intro.
### Dependencies
### Results



## Thanks

这篇readme 的结构参考：
[ConvNets Series. Actual Project Prototyping with Mask R-CNN](https://www.aiuai.cn/aifarm268.html#3.%E9%80%9A%E7%94%A850K%E7%AE%A1%E9%81%93General50KPipelineOverview)