# %%
import time
import cv2
import sys
sys.path.append('.')

from research.detection.mrcnn import visualize  # noqa: E402
from research.detection.configs import InferenceConfig  # noqa: E402
import research.detection.mrcnn.model as modellib  # noqa: E402


# %%
config = InferenceConfig()
config.display()

# Create model object in inference mode.
model = modellib.MaskRCNN(
    mode="inference", model_dir='data/MaskRCNN/build', config=config)
# Load weights trained on MS-COCO
model.load_weights('data/mask_rcnn_coco.h5', by_name=True)

# %%
# COCO Class names
# Index of the class in the list is its ID. For example, to get ID of
# the teddy bear class, use: class_names.index('teddy bear')
class_names = ['BG', 'person', 'bicycle', 'car', 'motorcycle', 'airplane',
               'bus', 'train', 'truck', 'boat', 'traffic light',
               'fire hydrant', 'stop sign', 'parking meter', 'bench', 'bird',
               'cat', 'dog', 'horse', 'sheep', 'cow', 'elephant', 'bear',
               'zebra', 'giraffe', 'backpack', 'umbrella', 'handbag', 'tie',
               'suitcase', 'frisbee', 'skis', 'snowboard', 'sports ball',
               'kite', 'baseball bat', 'baseball glove', 'skateboard',
               'surfboard', 'tennis racket', 'bottle', 'wine glass', 'cup',
               'fork', 'knife', 'spoon', 'bowl', 'banana', 'apple',
               'sandwich', 'orange', 'broccoli', 'carrot', 'hot dog', 'pizza',
               'donut', 'cake', 'chair', 'couch', 'potted plant', 'bed',
               'dining table', 'toilet', 'tv', 'laptop', 'mouse', 'remote',
               'keyboard', 'cell phone', 'microwave', 'oven', 'toaster',
               'sink', 'refrigerator', 'book', 'clock', 'vase', 'scissors',
               'teddy bear', 'hair drier', 'toothbrush']


# %%
print('capturing..')
# Load a random image from the images folder
# file_names = next(os.walk(IMAGE_DIR))[2]
# image = skimage.io.imread(os.path.join(IMAGE_DIR, random.choice(file_names)))
cap = cv2.VideoCapture(0)

while(1):
    # get a frame
    ret, frame = cap.read()
    # show a frame
    start = time.clock()
    results = model.detect([frame], verbose=1)
    r = results[0]
    # cv2.imshow("capture", frame)
    visualize.display_instances(frame, r['rois'], r['masks'], r['class_ids'],
                                class_names, r['scores'])
    end = time.clock()
    print(end-start)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

# image= cv2.imread("C:\\Users\\18301\\Desktop\\Mask_RCNN-master\\images\\9.jpg")
# Run detection
#
# results = model.detect([image], verbose=1)
#
# print(end-start)
# Visualize results
# r = results[0]
# visualize.display_instances(image, r['rois'], r['masks'], r['class_ids'],
#                            class_names, r['scores'])
