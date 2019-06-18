import inspect


def _get_func(obj):
    return f'{obj.__class__.__name__}.{inspect.stack()[1][3]}'


class MyClass:
    def function_one(self):
        print(_get_func(self))


if __name__ == "__main__":
    myclass = MyClass()
    myclass.function_one()
