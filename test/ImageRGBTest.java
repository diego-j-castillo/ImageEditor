import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import model.ImageModel;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the ImageRGB class.
 */
public class ImageRGBTest {
  //Examples:
  private int[][][] pixel1;
  private int[][][] pixel2;
  private ImageModel image1;
  private ImageModel image2;

  //sets to initial conditions
  @Before
  public void initial() {
    //5 x 4 image
    this.pixel1 = new int[5][4][3];
    Random rand1 = new Random(1);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        this.pixel1[w][h][0] = rand1.nextInt(255);
        this.pixel1[w][h][1] = rand1.nextInt(255);
        this.pixel1[w][h][2] = rand1.nextInt(255);
      }
    }
    //4 x 5 image
    this.pixel2 = new int[4][5][3];
    Random rand2 = new Random(1);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        this.pixel2[w][h][0] = rand2.nextInt(255);
        this.pixel2[w][h][1] = rand2.nextInt(255);
        this.pixel2[w][h][2] = rand2.nextInt(255);
      }
    }
    this.image1 = new ImageRGB(this.pixel1, 255);
    this.image2 = new ImageRGB(this.pixel2, 255);
  }

  //tests that we can get the width properly
  @Test
  public void testGetWidth() {
    assertEquals(5, this.image1.getWidth());
    assertEquals(4, this.image2.getWidth());
  }

  //tests that we can get the height properly
  @Test
  public void testGetHeight() {
    assertEquals(4, this.image1.getHeight());
    assertEquals(5, this.image2.getHeight());
  }

  //tests that this method always returns 3 for R, G, and B
  @Test
  public void testGetNumComponents() {
    assertEquals(3, this.image1.getNumComponents());
    assertEquals(3, this.image2.getNumComponents());
  }

  //tests that we can properly get the max color value
  @Test
  public void testGetMaxColorValue() {
    assertEquals(255, this.image1.getMaxColorValue());
    assertEquals(255, this.image2.getMaxColorValue());
    assertEquals(9000, new ImageRGB(this.pixel1, 9000).getMaxColorValue());
  }

  //tests that the copy made has the equal values for all components but doesn't produce a reference
  @Test
  public void testGetImageCopy() {
    ImageModel copyOne = this.image1.getImageCopy();
    ImageModel copyTwo = this.image2.getImageCopy();
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 0), copyOne.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 1), copyOne.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 2), copyOne.getComponentValue(w, h, 2));
      }
    }
    //does not return a reference since equals() was not overridden
    assertNotEquals(this.image1, copyOne);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        assertEquals(this.image2.getComponentValue(w, h, 0), copyTwo.getComponentValue(w, h, 0));
        assertEquals(this.image2.getComponentValue(w, h, 1), copyTwo.getComponentValue(w, h, 1));
        assertEquals(this.image2.getComponentValue(w, h, 2), copyTwo.getComponentValue(w, h, 2));
      }
    }
    //does not return a reference since equals() was not overridden
    assertNotEquals(this.image2, copyTwo);
  }

  //tests that image construct correctly and that we can accurately get the component desired
  @Test
  public void testGetComponentValue() {
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.pixel1[w][h][0], this.image1.getComponentValue(w, h, 0));
        assertEquals(this.pixel1[w][h][1], this.image1.getComponentValue(w, h, 1));
        assertEquals(this.pixel1[w][h][2], this.image1.getComponentValue(w, h, 2));
      }
    }
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        assertEquals(this.pixel2[w][h][0], this.image2.getComponentValue(w, h, 0));
        assertEquals(this.pixel2[w][h][1], this.image2.getComponentValue(w, h, 1));
        assertEquals(this.pixel2[w][h][2], this.image2.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can set the value of a component and that we can clamp up and down to the limits
  @Test
  public void testSetComponentValue() {
    assertEquals(15, this.image1.getComponentValue(0, 0, 0));
    assertEquals(78, this.image1.getComponentValue(1, 0, 1));
    assertEquals(124, this.image1.getComponentValue(0, 1, 2));
    this.image1.setComponentValue(0, 0, 0, 91);
    this.image1.setComponentValue(1, 0, 1, 64);
    this.image1.setComponentValue(0, 1, 2, 63);
    assertEquals(91, this.image1.getComponentValue(0, 0, 0));
    assertEquals(64, this.image1.getComponentValue(1, 0, 1));
    assertEquals(63, this.image1.getComponentValue(0, 1, 2));
    //checks that we can clamp to 0 if we go below
    this.image1.setComponentValue(0, 0, 0, -91);
    assertEquals(0, this.image1.getComponentValue(0, 0, 0));
    //checks that we can clamp to max value if we go above
    this.image1.setComponentValue(1, 0, 1, 1000);
    assertEquals(255, this.image1.getComponentValue(1, 0, 1));
  }
}

