# import os
# import sys
# os.sys.path.append(os.path.abspath("."))
from detection.workbench import Workbench

if __name__ == "__main__":
    w = Workbench()

    w.init_model('inference')
    w.detect(w.random_img())
    w.eval()

    # w.init_model('training')
    # w.train(epoch=2, learning_rate_coefficient=1, layers='heads')

