package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * A command that allows for an image to be filtered using some type of kernel.
 */
public class Filter extends ACommand {
  //filter that this command will do an image
  private final Filters f;

  /**
   * Command to apply a filter on an image, using an odd-dimension kernel to apply the filter.
   *
   * @param f      the filter that we are to apply to the image in the model; the kernel
   * @param source name of the image in the model we apply the filter on
   * @param place  the name we stroe the new image as
   */
  public Filter(Filters f, String source, String place) {
    super(source, place);
    if (f == null) {
      throw new IllegalArgumentException("Filter can not be null");
    }
    this.f = f;
  }

  /**
   * Filters an image based on the filter specified when this command was constructed.
   * Should save the new image to the command's specified save location. Filtering is based on
   * one of the predefined kernels for this command. The model will get a copy of the old image
   * sorted in it, with the source remaining untouched in its old place.
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
    images.filterImage(this.place, this.f.getKernel());
    return image;
  }
}
