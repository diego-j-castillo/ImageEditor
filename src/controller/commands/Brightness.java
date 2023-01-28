package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Brightens or darkens the image through the given value by adding it to the given pixels.
 */
public class Brightness extends ACommand {
  //value to brighten/darken by (positive/negative respectively)
  private final int changeVal;

  /**
   * Constructs a with a given amount to brighten or dim (positive or negative) and
   * with the given source and placement keys.
   *
   * @param change     change to brightness as an integer
   * @param willDarken true if we will be dimming the image with this command
   * @param source     key for the source image
   * @param place      key for the new image
   */
  public Brightness(int change, boolean willDarken, String source, String place) {
    super(source, place);
    if (change < 0) {
      throw new IllegalArgumentException("Can not have negative change");
    }
    //since we will darken, we will turn the given change value negative
    if (willDarken) {
      this.changeVal = change * -1;
    } else {
      //not darkening, so the change value is kept as is for apply() method
      this.changeVal = change;
    }
  }

  /**
   * Brightens up the image by the key and saves it to another location. Should still keep
   * the source image as it was in its original location (unless the new photo is in the same one).
   * If the new value from changing the brightness exceeds a lower or upper limit, we clamp to one
   * of those bounds (0 or max color value for an image).
   *
   * @param images collection of images to look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException image does not support brightness change
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //gets a copy of the old image to keep the source unmodified
    ImageModel image = images.getImageAt(this.source).getImageCopy();
    //not enough components to do RGB brightness change
    if (image.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image type must support at least 3 components");
    }
    //goes through every pixel
    for (int width = 0; width < image.getWidth(); width = width + 1) {
      for (int height = 0; height < image.getHeight(); height = height + 1) {
        for (int comp = 0; comp < 3; comp = comp + 1) {
          int newVal = image.getComponentValue(width, height, comp) + this.changeVal;
          //changes the value by the specified amount, clamping if necessary
          image.setComponentValue(width, height, comp,
              Math.max(0, Math.min(image.getMaxColorValue(), newVal)));
        }
      }
    }
    images.setImageAt(image, this.place);
    return image;
  }
}
