import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import model.ImageModel;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests that exceptions from the ImageRGB class are properly thrown.
 */
public class ImageRGBExceptions {
  //Examples:
  private ImageModel image1;

  //sets to initial conditions
  @Before
  public void initial() {
    //5 x 4 image
    int[][][] pixel1 = new int[5][4][3];
    Random rand1 = new Random(1);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        pixel1[w][h][0] = rand1.nextInt(255);
        pixel1[w][h][1] = rand1.nextInt(255);
        pixel1[w][h][2] = rand1.nextInt(255);
      }
    }
    this.image1 = new ImageRGB(pixel1, 255);
  }

  //should throw an exception if given a null pixel 3D array
  @Test
  public void testNullPixelsException() {
    try {
      ImageModel smallIm = new ImageRGB(null, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Must not provide a null value for an image", e.getMessage());
    }
  }

  //should throw an exception if we give a pixel array that has less than 3 components per
  //pixel
  @Test
  public void testLessThan3Comps() {
    try {
      int[][][] small = new int[5][4][2];
      ImageModel smallIm = new ImageRGB(small, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Must have correct component amount", e.getMessage());
    }
  }

  //should throw an exception if we give a pixel array that has more than 3 components per
  //pixel
  @Test
  public void testMoreThan3Comps() {
    try {
      int[][][] big = new int[5][4][4];
      ImageModel bigIm = new ImageRGB(big, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Must have correct component amount", e.getMessage());
    }
  }

  //should throw an exception if we open an image that has a component value below 0
  @Test
  public void testStartingBelow0() {
    //does it for all the components
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][0] = -1;
      ImageModel badIm = new ImageRGB(bad, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][1] = -192;
      ImageModel badIm = new ImageRGB(bad, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][2] = -14;
      ImageModel badIm = new ImageRGB(bad, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
  }

  //should throw an exception if we open an image that has a component value above the max
  @Test
  public void testStartingAboveMax() {
    //does it for all the components
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][0] = 256;
      ImageModel badIm = new ImageRGB(bad, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][1] = 9000;
      ImageModel badIm = new ImageRGB(bad, 512);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
    try {
      int[][][] bad = new int[5][4][3];
      bad[3][2][2] = 300;
      ImageModel badIm = new ImageRGB(bad, 255);
      fail("Somehow successfully constructed");
    } catch (IllegalArgumentException e) {
      assertEquals("Component values must be between 0 and the max value, inclusive",
          e.getMessage());
    }
  }

  //tests that the getComponent methods throws an exception if the component does not exist for
  //the image as selected
  @Test
  public void testGetComponentNotExist() {
    //width below 0
    try {
      this.image1.getComponentValue(-1, 0, 0);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //width above limit
    try {
      this.image1.getComponentValue(5, 0, 0);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //height below 0
    try {
      this.image1.getComponentValue(0, -2, 0);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //height above limit
    try {
      this.image1.getComponentValue(0, 6, 0);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //component below 0
    try {
      this.image1.getComponentValue(0, 0, -3);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //component above 2
    try {
      this.image1.getComponentValue(0, 0, 3);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
  }

  //tests that the setComponent methods throws an exception if the component does not exist for
  //the image as selected
  @Test
  public void testSetComponentNotExist() {
    //width below 0
    try {
      this.image1.setComponentValue(-1, 0, 0, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //width above limit
    try {
      this.image1.setComponentValue(5, 0, 0, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //height below 0
    try {
      this.image1.setComponentValue(0, -2, 0, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //height above limit
    try {
      this.image1.setComponentValue(0, 6, 0, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //component below 0
    try {
      this.image1.setComponentValue(0, 0, -3, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
    //component above 2
    try {
      this.image1.setComponentValue(0, 0, 3, 1);
      fail("Didn't catch the error when supposed to");
    } catch (IllegalArgumentException e) {
      assertEquals("Given component does not exist", e.getMessage());
    }
  }
}
