from cells.compiler import Compiler
c = Compiler('test')
records = [c.build_record(
    '/home/sh/Downloads/师弟标注数据/unlabeled_supplement_imgs/酱牛肉_40/2_43.json', (50, 50))]
records = [record for record in c.augmentor(records)]
c.build_dataset(records)
