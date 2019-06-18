
import datetime
import torch
import onnx
from onnx_tf.backend import prepare
import tensorflow as tf
from tensorflow.python.platform import gfile
from .file_utils import rmdir_if_exists, create_if_not_exists


class Config():
    """
        Base configuration for database-kernel.
    """

    # basic defs.
    root = 'bin'
    build_path = f'{root}/build/Onnx'
    log_path = 'log/kernel_onnx.log'

    onnx_cache_dir = build_path

    auto_clean = False


class OnnxKernel():
    def __init__(self, config: Config):
        self.config = config
        self.onnx_model = None
        self._path = None

        if self.config.auto_clean:
            self.clean_build()
        create_if_not_exists(self.config.build_path)

        self.log('Onnx Kernel activated', config=self.config.__dict__)

    def log(self, command, verbose=True, **kwargs):
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        msgs = [f"[{t}]\t{command}:"]
        msgs.extend([f'\t\t\t{key} = {kwargs[key]}' for key in kwargs])
        with open(self.config.log_path, 'a') as f:
            for msg in msgs:
                if verbose:
                    print(msg)
                f.write(f'{msg}\n')

    def clean_build(self):
        rmdir_if_exists(self.config.build_path)
        self.log('kernel builds cleaned')

    def from_torch(self, torch_model, dummy_input=None, name=None):
        if not dummy_input:
            # dummy_input = torch.randn(10, 3, 224, 224)
            dummy_input = torch.randn(10, 3, 128, 128, device='cuda')
        if name:
            self._path = f'{self.config.onnx_cache_dir}/{name}'
        else:
            self._path = f'{self.config.onnx_cache_dir}/{torch_model._get_name()}'
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
