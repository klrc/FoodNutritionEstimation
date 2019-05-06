
# 测试目录

## 5.6.c detection keras模型测试
[code](../demo/test_detection_keras_resized.py)
    
    确认了keras原模型对于resize操作的处理方式
    200×150的原图，在模型强制转化为128×128的情况下，

    resize / 模型内部方法效果差距明显
    后发现模型内部的方法并不是resize，而是等比缩放后补边
    
    存在过拟合的隐患，但是没有其他证据证明

---
## 5.6.b detection keras模型测试
[code](../demo/resized_comp.py)

    两张具有相同信息的图片，分别按约100%/60%缩放，
    仅图片的屏占比不同，
    测试结果 mask质量差距很大
    
    推测由于rpn输出的anchor大小不同，
    导致后续网络的处理精度差距大

    可能需要某种在识别出anchor之后resize的方法？

---
## 5.6.a detection keras2tf 测试
[code](../demo/convert_keras_to_tf.py)

    keras .h5 转为 tensorflow .pb ;
    一切顺利

---
## 5.6.d detection tensorflow .pb 输出测试
[code](../demo/test_pb_model.py)

    测试了压缩后的.pb模型性能
    正常输出情况下 基本没问题
    不太乐观的情况是 对于识别效果较差的图 网络没有任何输出
    
    可能需要从 output_4 关于mask的输出解决
    只有这个输出同时含有全信息且不会全0输出

    x.shape
    (1, 100, 28, 28, 31)

    也许能从这里得到次要目标

---
## 5.6.e detection .pb 转 tensorflow lite 测试
    
    可能要把预参数的计算也放到图里 有点复杂

---
## 5.6.f 体积估算 torch 转 keras .h5 测试

---
## 5.6.g 体积估算 准确率测试