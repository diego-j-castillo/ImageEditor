package controller.commands;

import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Represents a process that can be carried out on an image to manipulate and/or enhance it.
 * Each Process should be able to apply its function based on the specifics of the implementation.
 */
public interface Command {
  /**
   * Modifies/carries out process on an image in the collection. Implementations should attempt to
   * keep the source image unmodified and make a copy with the changes.
   *
   * @param images collection of images we will look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException image specified in collection does not support operation
   */
  ImageModel apply(ImageProcessorModel images);

  /**
   * Returns the location of the image by its key.
   *
   * @return the key for the image to be created
   */
  String getPlace();
}
