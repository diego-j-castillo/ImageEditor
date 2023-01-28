package view;

import java.io.IOException;

import model.ImageModel;

/**
 * Interface for the viewing of images. Numbering of rows and columns for pixels
 * in view starts from 1 to make it easier to understand. Implementations should be able
 * to look through a bunch of different images
 */
public interface ImageProcessorView {
  /**
   * Visualizes the image and outputs it to the destination.
   *
   * @param image image that we are to render
   * @param key   what we refer to the image as
   * @throws IOException could not properly transmit output
   */
  void renderImage(ImageModel image, String key) throws IOException;

  /**
   * Outputs a message to the selected destination.
   *
   * @param msg message to be output
   * @throws IOException could not properly transmit to output
   */
  void renderMessage(String msg) throws IOException;
}
