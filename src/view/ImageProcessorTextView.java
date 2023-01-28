package view;

import java.io.IOException;

import model.ImageModel;

/**
 * Implementation of the view that uses primarily text as a means to display information. Needs an
 * Image to source its view from and an Appendable for an output destination.
 */
public class ImageProcessorTextView implements ImageProcessorView {
  //destination for all of our output
  private final Appendable out;

  /**
   * Constructs a view that uses a given map of models but defaults to outputting to the system.
   *
   * @param out output destination for the view
   * @throws IllegalArgumentException images provided are null
   */
  public ImageProcessorTextView(Appendable out) {
    if (out == null) {
      throw new IllegalArgumentException("Destination provided can not be null");
    }
    this.out = out;
  }

  /**
   * Visualizes the image and outputs it to the destination.
   *
   * @param image image that we are to render
   * @param key   what we refer to the image as
   * @throws IOException could not properly transmit output
   */
  @Override
  public void renderImage(ImageModel image, String key) throws IOException {
    this.out.append(String.format("Image of size (%d, %d) stored as %s\n",
        image.getWidth(), image.getHeight(), key));
  }

  /**
   * Outputs a message to the selected destination.
   *
   * @param msg message to be output
   * @throws IOException could not properly transmit to output
   */
  @Override
  public void renderMessage(String msg) throws IOException {
    this.out.append(msg + "\n");
  }
}
