package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Represents a linear color transformation that occurs by transforming the components of a pixel
 * to linear combinations of each of its pixel's individual RGB components.
 */
public class Transform extends ACommand {
  //the transformation that this object will do on models and images
  private final Transformation tr;

  /**
   * Constructs a transformation. Specifies what type of transformation will occur, which image
   * it should transform, and what name the new image should be stored as.
   *
   * @param tr     transformation that will be done on the image and pixels
   * @param source source of the image in a model that we are transforming
   * @param place  name that image is stored as in a model
   * @throws IllegalArgumentException null value for transformation
   */
  public Transform(Transformation tr, String source, String place) {
    super(source, place);
    if (tr == null) {
      throw new IllegalArgumentException("Transformation can not be null");
    }
    this.tr = tr;
  }

  /**
   * Transforms an image based on the transformation specified when this command was constructed.
   * Should save the new image to the command's specified save location. Transformations get the new
   * component value by casting the new value to an integer, if needed. The original ImageModel
   * should remain untouched.
   *
   * @param images collection of images we will look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException image specified in collection does not support RGB
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //gets a copy of the image so that it does not modify the original
    ImageModel image = images.getImageAt(this.source).getImageCopy();
    //throws an exception if the image does not support 3 components
    if (image.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image type must support at least 3 components");
    }
    //sets the image and then does the right transformation on it
    images.setImageAt(image, this.place);
    images.transformImage(this.place, this.tr.getMatrix());
    return image;
  }
}
