package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Modifies an image by flipping it vertically. The amount of components that the image supports
 * does not matter since all of them should be moved in the flip.
 */
public class VFlip extends ACommand {
  /**
   * Constructs with the given source and placement keys.
   *
   * @param source key for the source image
   * @param place  key for the new image
   */
  public VFlip(String source, String place) {
    super(source, place);
  }

  /**
   * Vertically flips a specified image and saves it to another location. Should still keep
   * the source image as it was in its original location (unless the new photo is
   * in the same one).
   *
   * @param images collection we will look through
   * @return the final image after all modifications
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //gets copy of the source image so that it remains unmodified
    ImageModel image = images.getImageAt(this.source).getImageCopy();
    //goes through the top half image
    for (int width = 0; width < image.getWidth(); width = width + 1) {
      //does not go through the center row, if present, since that would not get flipped
      for (int height = 0; height < image.getHeight() / 2; height = height + 1) {
        //makes a pixel to store values for the flip
        int[] tempPixel = new int[image.getNumComponents()];
        for (int copy = 0; copy < image.getNumComponents(); copy = copy + 1) {
          tempPixel[copy] = image.getComponentValue(width, height, copy);
        }
        //actually flips the pixel with the corresponding one on the right and uses the temp to
        //flip the other one successfully
        for (int comp = 0; comp < image.getNumComponents(); comp = comp + 1) {
          image.setComponentValue(width, height, comp,
              image.getComponentValue(width, image.getHeight() - 1 - height, comp));
          image.setComponentValue(width, image.getHeight() - 1 - height, comp,
              tempPixel[comp]);
        }
      }
    }
    //sets the image in the model and returns it
    images.setImageAt(image, this.place);
    return image;
  }
}
