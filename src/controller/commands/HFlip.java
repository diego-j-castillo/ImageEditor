package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Modifies an image by flipping it horizontally. The amount of components that the image supports
 * does not matter since all of them should be moved in the flip.
 */
public class HFlip extends ACommand {
  /**
   * Constructs with the given source and placement keys.
   *
   * @param source key for the source image
   * @param place  key for the new image
   */
  public HFlip(String source, String place) {
    super(source, place);
  }

  /**
   * Flips the image specified horizontally and saves it to another location. Should still keep
   * the source image as it was in its original location (unless the new photo is in the same one).
   *
   * @param images collection of images to look through
   * @return the final image after all modifications
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //gets a copy of the image at that location to not modify the original
    ImageModel image = images.getImageAt(this.source).getImageCopy();
    //goes through the left half image
    for (int width = 0; width < image.getWidth() / 2; width = width + 1) {
      //does not go through the center column, if present, since that would not get flipped
      for (int height = 0; height < image.getHeight(); height = height + 1) {
        //makes a pixel to store values for the flip
        int[] tempPixel = new int[image.getNumComponents()];
        for (int copy = 0; copy < image.getNumComponents(); copy = copy + 1) {
          tempPixel[copy] = image.getComponentValue(width, height, copy);
        }
        //actually flips the pixel with the corresponding one on the right and uses the temp to
        //flip the other pixel
        for (int comp = 0; comp < image.getNumComponents(); comp = comp + 1) {
          image.setComponentValue(width, height, comp,
              image.getComponentValue(image.getWidth() - 1 - width, height, comp));
          image.setComponentValue(image.getWidth() - 1 - width, height, comp,
              tempPixel[comp]);
        }
      }
    }
    images.setImageAt(image, this.place);
    return image;
  }
}
