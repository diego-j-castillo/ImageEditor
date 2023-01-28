package view;

import java.awt.Image;

import controller.Features;

/**
 * Interface for a view that uses a GUI to display images and the options for modification.
 */
public interface ImageProcessorGUIView {
  /**
   * Renders out an image that will be displayed to the user along with its histogram.
   *
   * @param image the image that is to be visualized
   */
  void renderImage(Image image);

  /**
   * Renders out a message about any process done or any errors in processing images.
   *
   * @param msg the message that we are to render out
   */
  void renderMessage(String msg);

  /**
   * Renders out a histogram that can visualize any number of components/analyses of an image.
   *
   * @param histogram an array that represents the values in the histogram
   * @throws IllegalArgumentException histogram provided does not have enough components
   */
  void renderHistogram(int[][] histogram);

  /**
   * Adds the features to a given Features object by linking buttons or keys to each one, depending
   * on the implementation.
   *
   * @param features what we assign actions or keys to
   */
  void addFeatures(Features features);
}
