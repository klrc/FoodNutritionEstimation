from setuptools import find_packages, setup

#####

def readme():
    with open("README.md") as f:
        return f.read()


def requirements():
    with open("docker/requirements.txt") as f:
        return f.read()

