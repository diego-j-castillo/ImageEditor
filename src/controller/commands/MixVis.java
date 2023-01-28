package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Visualizes an image by its luma, value, or intensity component. Choosing of the visualization
 * type is determined by the String input (op).
 */
public class MixVis extends ACommand {
  //name of the operation, which can currently only be value
  private final String op;

  /**
   * Does the selected visualization process on an image in the model. The Image that we do the
   * operation on should support three component values for the RGB. Any calculations that require
   * converting from a non-integer use casting to make it an integer amount for the components.
   *
   * @param op     name of the operation as a String (value)
   * @param source name of the source image in the model
   * @param place  name of the image that we will generate when we place it in the model
   * @throws IllegalArgumentException type of mix is not an option
   */
  public MixVis(String op, String source, String place) {
    super(source, place);
    if (!op.equals("value")) {
      throw new IllegalArgumentException("Visualization not supported");
    }
    this.op = op;
  }

  /**
   * Modifies/carries out process on an image in the collection. Implementations should attempt to
   * keep the source image unmodified and make a copy with the changes.
   *
   * @param images collection of images we will look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException image specified in collection does not support operation
   * @throws IllegalArgumentException type of mix is not an option
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //gets copy of the source so that it remains unmodified
    ImageModel image = images.getImageAt(this.source).getImageCopy();
    //not enough components to do the visualization of mixing 3 components
    if (image.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image type must support at least 3 components");
    }
    //goes through each pixel
    for (int width = 0; width < image.getWidth(); width = width + 1) {
      for (int height = 0; height < image.getHeight(); height = height + 1) {
        //finds the max component per pixel
        int val;
        if (this.op.equals("value")) {
          //gets the value by using the maximum component in the pixel
          val = Math.max(image.getComponentValue(width, height, 0),
              Math.max(image.getComponentValue(width, height, 1),
                  image.getComponentValue(width, height, 2)));
        } else {
          //the operation as input to the constructor is not supported by apply()
          throw new IllegalArgumentException("Visualization not supported");
        }
        //sets all the components to the new value to visualize
        image.setComponentValue(width, height, 0, val);
        image.setComponentValue(width, height, 1, val);
        image.setComponentValue(width, height, 2, val);
      }
    }
    //sets the image and returns it
    images.setImageAt(image, this.place);
    return image;
  }
}
