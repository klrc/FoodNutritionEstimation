import os
import shutil


def exists(path):
    return os.path.exists(path)


def rmdir_if_exists(path):
    if exists(path):
        shutil.rmtree(path)
    return path


def create_if_not_exists(path):
    if not exists(path):
        os.makedirs(path)
    return path


def get_dir(path):
    return split_fp(path)[0]


def get_file_name(path):
    return split_fp(path)[1]


def split_fp(path):
    fp_dir = '/'.join(path.split('/')[0:-1])
    fp_file = path.split('/')[-1]
    return fp_dir, fp_file
