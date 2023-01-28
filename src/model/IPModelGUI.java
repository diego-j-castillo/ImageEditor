package model;

import java.awt.image.BufferedImage;

/**
 * An interface for a collection of images. It has the ability to get, add, and remove images from
 * the collection. It also has the ability to transform and filter images. Unlike the original
 * ImageProcessorModel interface, this model also has support for things that might be useful for
 * a GUI application of hte processor, such as producing a histogram and an Image from a
 * stored ImageModel.
 */
public interface IPModelGUI extends ImageProcessorModel {
  /**
   * Outputs a histogram as an array. The array is structured as [component] x [value], with the
   * components being RGB and then greyscale.
   *
   * @param key the name that the image is stored as to get a histogram from
   * @return the histogram as an array
   * @throws IllegalArgumentException key does not match any of the images
   */
  int[][] getHistogram(String key) throws IllegalArgumentException;

  /**
   * Creates an Image out of the ImageModel stored by a certain name. Used typically by GUI
   * programs to render the image that is being worked on currently.
   *
   * @param key the name that the image is stored as to get a histogram from
   * @return the ImageModel as an Image that can be used for some GUIs in Swing
   * @throws IllegalArgumentException key does not match any of the images
   * @throws IllegalArgumentException image does not support RGB
   */
  BufferedImage convertImage(String key) throws IllegalArgumentException;
}
