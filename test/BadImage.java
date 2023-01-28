import model.AbstractImage;
import model.ImageModel;

/**
 * Image implementation that has no restrictions as to how it can be constructed. Primarily to be
 * used to test modifications for their ability to filter out images that do not support the
 * operation.
 */
public class BadImage extends AbstractImage {
  /**
   * Constructs an image with no limit, besides that the components of each pixel are within limits.
   *
   * @param pixels 3D array of pixels for the image
   * @param maxColorVal the maximum number value for a component
   * @param numComponents number of components per image
   * @throws IllegalArgumentException must have right component count
   * @throws IllegalArgumentException components must fit within range
   */
  public BadImage(int[][][] pixels, int maxColorVal, int numComponents) {
    super(pixels, maxColorVal, numComponents);
  }

  /**
   * Constructs an image of this type and returns it. This returns a BadImage that has the given
   * pixels, the same max value, and the same number of components.
   *
   * @param pixels pixels for the image
   */
  @Override
  protected ImageModel constructImage(int[][][] pixels) {
    return new BadImage(pixels, this.maxColorVal, this.numComponents);
  }
}
