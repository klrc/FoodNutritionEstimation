
import datetime
import torch
import os
import onnx
from onnx_tf.backend import prepare
import tensorflow as tf
from tensorflow.python.platform import gfile
import shutil


class OnnxKernel():
    def __init__(self, path='data/Onnx/build', clean=True):
        self._dir = path
        self._path = None
        self.onnx_model = None

        if clean:
            self.clean_build()
        self.create_build(path)
        self.log('Onnx Kernel activated in', path)

    def log(self, message, target):
        # 记录事务日志
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        msg = f'[{t}] {message}: {target}'
        print(msg)

    def create_build(self, path):
        self.__create_dir(path)

    def clean_build(self):
        if os.path.exists(self._dir):
            shutil.rmtree(self._dir)
        self.log('kernel builds cleaned', self._dir)

    def __create_dir(self, path=None):
        if not os.path.exists(path):
            os.makedirs(path)
        self.log('build dir:', path)
        return path

    def from_torch(self, torch_model, dummy_input=None):
        if not dummy_input:
            dummy_input = torch.randn(10, 3, 224, 224, device='cuda')
        self._path = f'{self._dir}/{torch_model._get_name()}'
        torch.onnx.export(torch_model, dummy_input,
                          f'{self._path}.onnx', verbose=True)
        self.onnx_model = onnx.load(f'{self._path}.onnx')
        return self.onnx_model

    def to_tensorflow(self, verbose=True):
        if self.onnx_model:
            tf_rep = prepare(self.onnx_model)
            tf_rep.export_graph(f'{self._path}.pb')
            if verbose:
                self.show_pb(f'{self._path}.pb')

    def show_pb(self, path):
        with tf.Session():
            print("load graph")
            with gfile.FastGFile(path, 'rb') as f:
                graph_def = tf.GraphDef()
                # Note: one of the following two lines work if required libraries are available
                # text_format.Merge(f.read(), graph_def)
                graph_def.ParseFromString(f.read())
                tf.import_graph_def(graph_def, name='')
                for i, n in enumerate(graph_def.node):
                    print("Name of the node - %s" % n.name)
