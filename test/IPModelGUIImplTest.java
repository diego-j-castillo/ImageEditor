import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import model.IPModelGUI;
import model.IPModelGuiImpl;
import model.ImageModel;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the IPModelGUIImpl class.
 */
public class IPModelGUIImplTest {
  //Examples:
  private ImageModel image1;
  private IPModelGUI model;

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
    this.model = new IPModelGuiImpl();
    this.model.setImageAt(this.image1, "image1");
  }

  //tests the ability for an image to return a histogram
  @Test
  public void testGetHistogram() {
    int[][] histo1 = this.model.getHistogram("image1");
    //checks that the size of the histogram is right
    assertEquals(4, histo1.length);
    assertEquals(256, histo1[0].length);
    //goes through and checks we have the right total entries per component, 20 for a 5x4 image
    for (int w = 0; w < 4; w = w + 1) {
      int sum = 0;
      for (int h = 0; h < 256; h = h + 1) {
        sum = sum + histo1[w][h];
      }
      assertEquals(20, sum);
    }
  }

  //tests the ability to create an image by a given key
  @Test
  public void testConvertImage() {
    BufferedImage im1 = this.model.convertImage("image1");
    //checks the dimensions are right
    assertEquals(5, im1.getWidth());
    assertEquals(4, im1.getHeight());
    //checks that what is held in the image in components is right
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 0),
            new Color(im1.getRGB(w, h)).getRed());
        assertEquals(this.image1.getComponentValue(w, h, 1),
            new Color(im1.getRGB(w, h)).getGreen());
        assertEquals(this.image1.getComponentValue(w, h, 2),
            new Color(im1.getRGB(w, h)).getBlue());
      }
    }
  }

  //tests that we can handle an exception in both of the new methods
  @Test
  public void testExceptions() {
    ImageModel bad = new BadImage(new int[10][20][2], 255, 2);
    //getting the histogram of an image that is not in the model
    try {
      this.model.getHistogram("dsafdsafafefda");
      fail("somehow got the histogram for fake image");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    //getting conversion of image not in the model
    try {
      this.model.convertImage("fdsafsdgfdafdf");
      fail("somehow converted the image");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    //checks that we can't get conversion of image that does not have 3 components
    try {
      this.model.setImageAt(bad, "bad");
      this.model.convertImage("bad");
      fail("somehow converted the image");
    } catch (IllegalArgumentException e) {
      assertEquals("Image does not work for this image type", e.getMessage());
    }
  }
}
