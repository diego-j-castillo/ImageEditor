package model;

/**
 * Represents a collection of Images that can be modified as needed with commands input by a
 * controller. Should be able to add and remove new Images,and should ImageModels stored
 * when asked for a specific one in its collection.
 */
public interface ImageProcessorModel {
  /**
   * Gets the Image based on the key given by the key for it.
   *
   * @param key where we can find the image that we are looking for
   * @return copy of the image at the key
   * @throws IllegalArgumentException key does not match any of the images
   */
  ImageModel getImageAt(String key);

  /**
   * Sets the Image based on the key given by the key for it.
   *
   * @param image image to be placed into the collection
   * @param key   where we can find the image that we are looking for
   */
  void setImageAt(ImageModel image, String key);

  /**
   * Removes an Image based on the key given.
   *
   * @param key where we can find the image that we are looking for
   */
  void removeImageAt(String key);

  /**
   * Filters an image using the kernel that is supplied to the function. Kernels should be arranged
   * in the format of col x row, which matches the ImageModels that are in width x height.
   *
   * @param key    the name of the image that we are to filter
   * @param kernel the Image that is used to filter the image
   * @throws IllegalArgumentException image is not in model
   * @throws IllegalArgumentException kernel is not of odd dimensions
   */
  void filterImage(String key, double[][] kernel) throws IllegalArgumentException;

  /**
   * Does a linear transformation on the specified image, using the matrix.
   *
   * @param key     the name of the image in the model
   * @param tMatrix a 3 x 3 matrix to use to transform the RGB components
   * @throws IllegalArgumentException image is not in model
   * @throws IllegalArgumentException transformation matrix is not 3 x 3
   */
  void transformImage(String key, double[][] tMatrix) throws IllegalArgumentException;
}
