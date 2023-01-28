package model;

/**
 * Implementation of the Image interface. Stores each pixel in a 3D array of integers, with the
 * format being [x][y][RGB value]. This implementation uses 3 rgb values to store information
 * about a pixel. Individual component values go from 0 to a set limit, inclusive.
 */
public class ImageRGB extends AbstractImage {
  /**
   * Constructs the image by using a given 3D array of pixels.
   *
   * @param pixels      array to be used for the image
   * @param maxColorVal maximum integer value for a color, includes it and 0
   * @throws IllegalArgumentException must have 3 components in an ImageRGB
   * @throws IllegalArgumentException components must fit within range
   */
  public ImageRGB(int[][][] pixels, int maxColorVal) {
    super(pixels, maxColorVal, 3);
  }

  /**
   * Constructs an image of this type and returns it. Uses the max pixels of this image and
   * sets the number of components to 3.
   *
   * @param pixels pixels for the image
   */
  protected ImageModel constructImage(int[][][] pixels) {
    return new ImageRGB(pixels, this.maxColorVal);
  }
}
