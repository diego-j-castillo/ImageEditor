package model;

/**
 * Abstracts out common methods and aspects of the Image implementations. Used to simplify the
 * addition of new image types in the future that will likely have similar methods and fields.
 */
public abstract class AbstractImage implements ImageModel {
  //3D array of pixels
  protected final int[][][] pixels;
  //the maximum number value for an individual component in an image
  protected final int maxColorVal;
  //the amount of components in an image
  protected final int numComponents;

  /**
   * Constructs an Abstract image with a 3D array of pixels and a maximum value for each of
   * the components.
   *
   * @param pixels        pixels in the image
   * @param maxColorVal   maximum value for a color, which would be it and 0
   * @param numComponents number of components per pixel in the image
   * @throws IllegalArgumentException given pixels is null
   * @throws IllegalArgumentException must have 3 components in an ImageRGB
   * @throws IllegalArgumentException components must fit within range
   */
  public AbstractImage(int[][][] pixels, int maxColorVal, int numComponents) {
    //can not make an image with a null pixel array
    if (pixels == null) {
      throw new IllegalArgumentException("Must not provide a null value for an image");
    }
    //the number of components does not match the array
    if (pixels[0][0].length != numComponents) {
      throw new IllegalArgumentException("Must have correct component amount");
    }
    this.pixels = new int[pixels.length][pixels[0].length][numComponents];
    //checks that component values work for image limits (0 <= val <= maxColorVal)
    for (int width = 0; width < pixels.length; width = width + 1) {
      for (int height = 0; height < pixels[0].length; height = height + 1) {
        for (int comp = 0; comp < numComponents; comp = comp + 1) {
          if (pixels[width][height][comp] < 0 || pixels[width][height][comp] > maxColorVal) {
            throw new IllegalArgumentException(
                "Component values must be between 0 and the max value, inclusive");
          }
          this.pixels[width][height][comp] = pixels[width][height][comp];
        }
      }
    }
    this.maxColorVal = maxColorVal;
    this.numComponents = numComponents;
  }

  /**
   * Gets the width of the image in pixels.
   *
   * @return width in pixels of image as an int
   */
  @Override
  public int getWidth() {
    return this.pixels.length;
  }

  /**
   * Gets the height of the image in pixels.
   *
   * @return height in pixels of image as an int
   */
  @Override
  public int getHeight() {
    return this.pixels[0].length;
  }

  /**
   * Gets the number of components in an image.
   *
   * @return the number of components for the image type
   */
  @Override
  public int getNumComponents() {
    return this.numComponents;
  }

  /**
   * Returns the max integer value for a components for this image.
   *
   * @return integer maximum value of any color component
   */
  @Override
  public int getMaxColorValue() {
    return this.maxColorVal;
  }

  /**
   * Gets an exact copy of an image.
   *
   * @return copy of the image as an image
   */
  @Override
  public ImageModel getImageCopy() {
    //makes a copy of the pixels by assigning the same values of this image's pixels to a new
    //pixel 3D array
    int[][][] pixelCopy = new int[this.getWidth()][this.getHeight()][this.numComponents];
    for (int w = 0; w < this.getWidth(); w = w + 1) {
      for (int h = 0; h < this.getHeight(); h = h + 1) {
        for (int c = 0; c < this.numComponents; c = c + 1) {
          pixelCopy[w][h][c] = this.pixels[w][h][c];
        }
      }
    }
    //calls this method so that we return the right Image implementation
    return this.constructImage(pixelCopy);
  }

  /**
   * Constructs an image of this type and returns it. The specific type of the Image return is
   * dependent on the implementation.
   *
   * @param pixels pixels for the image
   */
  protected abstract ImageModel constructImage(int[][][] pixels);

  /**
   * Gets the value of the specified component for a specified pixel.
   *
   * @param width  column in which the pixel is located
   * @param height row in which the pixel is located
   * @param comp   component that is wanted
   * @return the value of the specified component for a pixel as an integer
   * @throws IllegalArgumentException specified location/component does not exist
   */
  @Override
  public int getComponentValue(int width, int height, int comp) {
    //component does not exist for this type of image
    if (width < 0 || width >= this.getWidth() || height < 0 || height >= this.getHeight()
        || comp < 0 || comp >= this.numComponents) {
      throw new IllegalArgumentException("Given component does not exist");
    }
    //returns the image directly since we might not want a copy always
    return this.pixels[width][height][comp];
  }

  /**
   * Sets the value of the specified component for a specified pixel. Clamps the value to
   * 0 or to the max if the given exceeds either bound.
   *
   * @param width  column in which the pixel is located
   * @param height row in which the pixel is located
   * @param comp   component that is to be modified
   * @param val    new value for the component
   * @throws IllegalArgumentException specified location/component does not exist
   */
  @Override
  public void setComponentValue(int width, int height, int comp, int val) {
    //component does not exist for this type of image
    if (width < 0 || width >= this.getWidth() || height < 0 || height >= this.getHeight()
        || comp < 0 || comp >= this.numComponents) {
      throw new IllegalArgumentException("Given component does not exist");
    }
    //clamps up or down to fit the image limits
    if (val < 0) {
      this.pixels[width][height][comp] = 0;
    } else if (val > this.maxColorVal) {
      this.pixels[width][height][comp] = this.maxColorVal;
    } else {
      this.pixels[width][height][comp] = val;
    }
  }
}
