package controller.commands;

/**
 * Represents one of the possible filters that could be applied on an image, and its corresponding
 * matrix. The kernel used should be arranged in col * row, which matches how ImageModel's should
 * be in width * height.
 */
public enum Filters {
  //for blurring an image
  Blur(new double[][]{
      {1 / 16.0, 1 / 8.0, 1 / 16.0},
      {1 / 8.0, 1 / 4.0, 1 / 8.0},
      {1 / 16.0, 1 / 8.0, 1 / 16.0}
  }),
  //for sharpening an image
  Sharpen(new double[][]{
      {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0},
      {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
      {-1 / 8.0, 1 / 4.0, 1.0, 1 / 4.0, -1 / 8.0,},
      {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0,},
      {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0,}
  });

  //the matrix that we are to use for filtering
  private final double[][] matrix;

  /**
   * A filter as designated by a name for the filter and a kernel. The kernel used should be
   * arranged in col * row, which matches how ImageModel's should be in width * height.
   *
   * @param matrix the kernel that is to be used for this type of filter
   */
  Filters(double[][] matrix) {
    this.matrix = matrix;
  }

  /**
   * Used to get the kernel for this type of filter.
   *
   * @return a copy of the kernel as a 2D array of double values
   */
  public double[][] getKernel() {
    double[][] out = new double[this.matrix.length][this.matrix[0].length];
    for (int col = 0; col < this.matrix.length; col = col + 1) {
      for (int row = 0; row < this.matrix[0].length; row = row + 1) {
        out[col][row] = this.matrix[col][row];
      }
    }
    return out;
  }
}
