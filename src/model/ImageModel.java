package model;

/**
 * Interface that represents an image. An image has a width and a height, both in pixels. Each
 * pixel has a specific amount of components, depending on the implementation (3 in RGB, 4 for
 * transparency in PNG). Implementations should also specify which modifications they allow
 * upon themselves. Indexing of components begins from 0 for all implementations.
 */
public interface ImageModel {
  /**
   * Gets the width of the image in pixels.
   *
   * @return width in pixels of image as an int
   */
  int getWidth();

  /**
   * Gets the height of the image in pixels.
   *
   * @return height in pixels of image as an int
   */
  int getHeight();

  /**
   * Gets the number of components in an image.
   *
   * @return the number of components for the image type
   */
  int getNumComponents();

  /**
   * Returns the max integer value for a components for this image.
   *
   * @return integer maximum value of any color component
   */
  int getMaxColorValue();

  /**
   * Gets an exact copy of an image.
   *
   * @return copy of the image as an image
   */
  ImageModel getImageCopy();

  /**
   * Gets the value of the specified component for a specified pixel.
   *
   * @param width column in which the pixel is located
   * @param height row in which the pixel is located
   * @param comp component that is wanted
   * @return the value of the specified component for a pixel as an integer
   */
  int getComponentValue(int width, int height, int comp);

  /**
   * Sets the value of the specified component for a specified pixel.
   *
   * @param width column in which the pixel is located
   * @param height row in which the pixel is located
   * @param comp component that is to be modified
   * @param val new value for the component
   */
  void setComponentValue(int width, int height, int comp, int val);
}
