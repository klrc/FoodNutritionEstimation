def read_image(path):
    """Load the specified image and return a [H,W,3] Numpy array.
    """
    import skimage
    # Load image
    image = skimage.io.imread(path)
    image = skimage.transform.resize(image, (128, 128))

    # If grayscale. Convert to RGB for consistency.
    if image.ndim != 3:
        image = skimage.color.gray2rgb(image)
    # If has an alpha channel, remove it for consistency
    if image.shape[-1] == 4:
        image = image[..., :3]
    return image
