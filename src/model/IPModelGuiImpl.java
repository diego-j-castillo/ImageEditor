package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Implementation of the IPModelGUI interface. Uses an ImageProcessorModel to carry out functions
 * from that interface. Outputs histograms with 4 components (RGB and intensity) and converts
 * ImageModels stored as BufferedImages.
 */

public class IPModelGuiImpl implements IPModelGUI {
  //what we delegate some methods to
  private final ImageProcessorModel model;

  /**
   * Constructs a model, adding an ImageProcessorModel that some methods will be delegated to.
   */
  public IPModelGuiImpl() {
    this.model = new ImageProcessorModelImpl();
  }

  /**
   * Outputs a histogram as an array. The array is structured as [component] x [value], with the
   * components being RGB and then greyscale.
   *
   * @param key the name that the image is stored as to get a histogram from
   * @return the histogram as an array
   * @throws IllegalArgumentException key does not match any of the images
   */
  @Override
  public int[][] getHistogram(String key) throws IllegalArgumentException {
    ImageModel base = this.model.getImageAt(key);
    int[][] histo = new int[4][base.getMaxColorValue() + 1];
    for (int w = 0; w < base.getWidth(); w = w + 1) {
      for (int h = 0; h < base.getHeight(); h = h + 1) {
        //adds one to that specific value for a component
        histo[0][base.getComponentValue(w, h, 0)] += 1;
        histo[1][base.getComponentValue(w, h, 1)] += 1;
        histo[2][base.getComponentValue(w, h, 2)] += 1;
        //does the intensity as the fourth one
        int intensity = (base.getComponentValue(w, h, 0) + base.getComponentValue(w, h, 1)
            + base.getComponentValue(w, h, 2)) / 3;
        histo[3][intensity] += 1;
      }
    }
    return histo;
  }

  /**
   * Creates an Image out of the ImageModel stored by a certain name. Used typically by GUI
   * programs to render the image that is being worked on currently.
   *
   * @param key the name that the image is stored as to get a histogram from
   * @return the ImageModel as an Image that can be used for some GUIs in Swing
   * @throws IllegalArgumentException key does not match any of the images
   * @throws IllegalArgumentException image does not support RGB
   */
  @Override
  public BufferedImage convertImage(String key) {
    ImageModel base = this.model.getImageAt(key);
    //check that we have enough components for conversion
    if (base == null || base.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image does not work for this image type");
    }
    //the bufferedImage version of the ImageModel object we are trying to convert
    BufferedImage end = new BufferedImage(base.getWidth(), base.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    //goes through the ImageModel and puts data of each pixel to the BufferedImage
    for (int w = 0; w < base.getWidth(); w = w + 1) {
      for (int h = 0; h < base.getHeight(); h = h + 1) {
        //fits the values so that it is in a 255 scale
        int red = (int) (base.getComponentValue(w, h, 0) / (base.getMaxColorValue() / 255.0));
        int green = (int) (base.getComponentValue(w, h, 1) / (base.getMaxColorValue() / 255.0));
        int blue = (int) (base.getComponentValue(w, h, 2) / (base.getMaxColorValue() / 255.0));
        end.setRGB(w, h, new Color(red, green, blue).getRGB());
      }
    }
    return end;
  }

  /**
   * Gets the Image based on the key given by the key for it.
   *
   * @param key where we can find the image that we are looking for
   * @return copy of the image at the key
   * @throws IllegalArgumentException key does not match any of the images
   */
  @Override
  public ImageModel getImageAt(String key) {
    return this.model.getImageAt(key);
  }

  /**
   * Sets the Image based on the key given by the key for it.
   *
   * @param image image to be placed into the collection
   * @param key   where we can find the image that we are looking for
   */
  @Override
  public void setImageAt(ImageModel image, String key) {
    this.model.setImageAt(image, key);
  }

  /**
   * Removes an Image based on the key given.
   *
   * @param key where we can find the image that we are looking for
   */
  @Override
  public void removeImageAt(String key) {
    this.model.removeImageAt(key);
  }

  /**
   * Filters an image using the kernel that is supplied to the function. Kernels should be arranged
   * in the format of col x row, which matches the ImageModels that are in width x height.
   *
   * @param key    the name of the image that we are to filter
   * @param kernel the Image that is used to filter the image
   * @throws IllegalArgumentException image is not in model
   * @throws IllegalArgumentException kernel is not of odd dimensions
   */
  @Override
  public void filterImage(String key, double[][] kernel) throws IllegalArgumentException {
    this.model.filterImage(key, kernel);
  }

  /**
   * Does a linear transformation on the specified image, using the matrix.
   *
   * @param key     the name of the image in the model
   * @param tMatrix a 3 x 3 matrix to use to transform the RGB components
   * @throws IllegalArgumentException image is not in model
   * @throws IllegalArgumentException transformation matrix is not 3 x 3
   */
  @Override
  public void transformImage(String key, double[][] tMatrix) throws IllegalArgumentException {
    this.model.transformImage(key, tMatrix);
  }
}
