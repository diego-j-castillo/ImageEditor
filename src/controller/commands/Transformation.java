package controller.commands;

/**
 * Transformation for that can be done on a pixel for its RGB values. The matrix for the
 * transformations are arranged in [RGB comp] * [factors].
 */
public enum Transformation {
  //gets the luma value of a pixel
  Luma(new double[][]{
      {0.2126, 0.7152, 0.0722},
      {0.2126, 0.7152, 0.0722},
      {0.2126, 0.7152, 0.0722}
  }),
  //gets the average of all components in a pixel
  Intensity(new double[][]{
      {1 / 3.0, 1 / 3.0, 1 / 3.0},
      {1 / 3.0, 1 / 3.0, 1 / 3.0},
      {1 / 3.0, 1 / 3.0, 1 / 3.0}
  }),
  //visualizes the red component of pixels
  Red(new double[][]{
      {1.0, 0.0, 0.0},
      {1.0, 0.0, 0.0},
      {1.0, 0.0, 0.0}
  }),
  //visualizes the green component of pixels
  Green(new double[][]{
      {0.0, 1.0, 0.0},
      {0.0, 1.0, 0.0},
      {0.0, 1.0, 0.0}
  }),
  //visualizes the blue component of pixels
  Blue(new double[][]{
      {0.0, 0.0, 1.0},
      {0.0, 0.0, 1.0},
      {0.0, 0.0, 1.0}
  }),
  //transforms the image to have a sepia tone
  Sepia(new double[][]{
      {0.393, 0.769, 0.189},
      {0.349, 0.686, 0.168},
      {0.272, 0.534, 0.131}
  });

  //the 3x3 matrix to be used for the transformation
  private final double[][] matrix;

  /**
   * A linear color transformation that uses a 3x3 matrix to assign new values to each RGB
   * component in an image.
   *
   * @param matrix the matrix that is used for linear color transformations.
   */
  Transformation(double[][] matrix) {
    this.matrix = matrix;
  }

  /**
   * Used to get the matrix for this type of filter.
   *
   * @return a copy of the kernel as a 2D array of double values
   */
  public double[][] getMatrix() {
    double[][] out = new double[3][3];
    for (int r = 0; r < 3; r = r + 1) {
      for (int c = 0; c < 3; c = c + 1) {
        out[r][c] = this.matrix[r][c];
      }
    }
    return out;
  }
}
