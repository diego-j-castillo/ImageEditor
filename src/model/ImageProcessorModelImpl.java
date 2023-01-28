package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ImageProcessorModel interface. Uses a map to store the images. Allows for
 * the direct adding and removing of images. Also allows for the filtering and linear transforming
 * of the colors in an image.
 */
public class ImageProcessorModelImpl implements ImageProcessorModel {
  //collection of all the Images
  private final Map<String, ImageModel> images;

  /**
   * Constructs a model with a blank HashMap.
   */
  public ImageProcessorModelImpl() {
    this.images = new HashMap<String, ImageModel>();
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
    ImageModel out = this.images.getOrDefault(key, null);
    if (out == null) {
      throw new IllegalArgumentException("Desired Image could not be found by that key");
    }
    return out;
  }

  /**
   * Sets the Image based on the key given by the key for it.
   *
   * @param image image to be placed into the collection
   * @param key   where we can find the image that we are looking for
   */
  @Override
  public void setImageAt(ImageModel image, String key) {
    this.images.put(key, image);
  }

  /**
   * Removes an Image based on the key given.
   *
   * @param key where we can find the image that we are looking for
   */
  @Override
  public void removeImageAt(String key) {
    this.images.remove(key);
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
  public void filterImage(String key, double[][] kernel) {
    //checks that we match and that the dimensions are odd too
    if (kernel.length % 2 == 0 || kernel[0].length % 2 == 0) {
      throw new IllegalArgumentException("Kernel must have odd matching dimensions");
    }
    //gets the image and a base image to do the filtering on
    ImageModel image = this.images.get(key);
    //checks that the image exists in this model
    if (image == null) {
      throw new IllegalArgumentException("Desired Image could not be found by that key");
    }
    ImageModel base = image.getImageCopy();
    for (int width = 0; width < image.getWidth(); width = width + 1) {
      for (int height = 0; height < image.getHeight(); height = height + 1) {
        this.filterImageHelp(image, base, width, height, 0, kernel);
        this.filterImageHelp(image, base, width, height, 1, kernel);
        this.filterImageHelp(image, base, width, height, 2, kernel);
      }
    }
  }

  private void filterImageHelp(ImageModel end, ImageModel base,
                               int width, int height, int comp, double[][] kernel) {
    //stores half the kernel length
    int halfK = kernel.length / 2;
    //keeps track of the new value that the component will be
    double endVal = 0;
    //goes through the kernel, first going through columns that are inside the image
    for (int col = halfK * -1; col <= halfK; col = col + 1) {
      if (col + width >= 0 && col + width < end.getWidth()) {
        //then goes through the kernel rows that are inside the image
        for (int row = halfK * -1; row <= halfK; row = row + 1) {
          if (row + height >= 0 && row + height < end.getHeight()) {
            //adds the value to the running total, using the unmodified base
            endVal = endVal + (kernel[col + halfK][row + halfK]
                * base.getComponentValue(width + col, height + row, comp));
          }
        }
      }
    }
    //sets the final image component to it, clamping if needed
    end.setComponentValue(width, height, comp,
        Math.max(0, Math.min(base.getMaxColorValue() ,(int) endVal)));
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
  public void transformImage(String key, double[][] tMatrix) {
    if (tMatrix.length != 3 || tMatrix[0].length != 3) {
      throw new IllegalArgumentException("Transformation must use 3x3 matrix");
    }
    ImageModel image = this.images.get(key);
    //checks if image is in the model
    if (image == null) {
      throw new IllegalArgumentException("Desired Image could not be found by that key");
    }
    //goes through each pixel in the image
    for (int width = 0; width < image.getWidth(); width = width + 1) {
      for (int height = 0; height < image.getHeight(); height = height + 1) {
        //temporarily stores the base values for each component
        int rBase = image.getComponentValue(width, height, 0);
        int gBase = image.getComponentValue(width, height, 1);
        int bBase = image.getComponentValue(width, height, 2);
        //gets the new value for the components
        int rVal = (int) (tMatrix[0][0] * rBase + tMatrix[0][1] * gBase + tMatrix[0][2] * bBase);
        int gVal = (int) (tMatrix[1][0] * rBase + tMatrix[1][1] * gBase + tMatrix[1][2] * bBase);
        int bVal = (int) (tMatrix[2][0] * rBase + tMatrix[2][1] * gBase + tMatrix[2][2] * bBase);
        //sets all the components to the new value to visualize, and clamps if needed
        image.setComponentValue(width, height, 0,
            Math.max(0, Math.min(rVal, image.getMaxColorValue())));
        image.setComponentValue(width, height, 1,
            Math.max(0, Math.min(gVal, image.getMaxColorValue())));
        image.setComponentValue(width, height, 2,
            Math.max(0, Math.min(bVal, image.getMaxColorValue())));
      }
    }
  }
}
