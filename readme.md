# Food Nutrition Estimation
[img NA]

## Introduction
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

## Stage1 - MaskRCNN Detection 食物识别
missing intro.
### Dependencies
### Training
### Evaluating
### Results

## Stage2 - Volume Estimation 体积估算
missing intro.
### Dependencies
### Training
### Evaluating
### Results

## Stage3 - Nutrient Reference 营养成分推断
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