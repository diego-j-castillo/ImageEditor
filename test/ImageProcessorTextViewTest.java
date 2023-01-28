import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import model.ImageModel;
import model.ImageRGB;
import view.ImageProcessorTextView;
import view.ImageProcessorView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the ImageProcessorTextView class.
 */
public class ImageProcessorTextViewTest {
  private ImageModel image1;
  //output destination for testing
  private StringBuilder out;
  private ImageProcessorView view;

  //sets to initial conditions
  @Before
  public void initial() {
    //5 x 4 image
    //Examples:
    int[][][] pixel1 = new int[5][4][3];
    Random rand1 = new Random(1);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        pixel1[w][h][0] = rand1.nextInt(255);
        pixel1[w][h][1] = rand1.nextInt(255);
        pixel1[w][h][2] = rand1.nextInt(255);
      }
    }
    //4 x 5 image
    int[][][] pixel2 = new int[4][5][3];
    Random rand2 = new Random(1);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        pixel2[w][h][0] = rand2.nextInt(255);
        pixel2[w][h][1] = rand2.nextInt(255);
        pixel2[w][h][2] = rand2.nextInt(255);
      }
    }
    this.image1 = new ImageRGB(pixel1, 255);
    ImageModel image2 = new ImageRGB(pixel2, 255);
    //sets up the view
    this.out = new StringBuilder();
    this.view = new ImageProcessorTextView(this.out);
  }

  //checks for the exceptions from the 1 argument constructor
  @Test
  public void test1ArgConstructor() {
    try {
      ImageProcessorView bad = new ImageProcessorTextView(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Destination provided can not be null", e.getMessage());
    }
  }

  //tests that renderImage() works
  @Test
  public void testRenderImage() {
    try {
      this.view.renderImage(this.image1, "image1");
      assertEquals("Image of size (5, 4) stored as image1\n", this.out.toString());
    } catch (IOException e) {
      fail("could not transmit");
    }
  }

  //tests that we can render a message
  @Test
  public void testRenderMessage() {
    try {
      this.view.renderMessage("lettuce milk radish");
      this.view.renderMessage("comprehend loam");
      assertEquals("lettuce milk radish\ncomprehend loam\n", out.toString());
    } catch (IOException e) {
      fail("could not render message");
    }
  }

  //tests that we can throw the right exception and message if we have a corrupted Appendable
  @Test
  public void testCorruptedOut() {
    try {
      ImageProcessorView badView = new ImageProcessorTextView(new CorruptedAppendable());
      badView.renderMessage("lettuce");
      fail("Somehow could render the message");
    } catch (IOException e) {
      assertEquals("IOException thrown", e.getMessage());
    }
    try {
      ImageProcessorView badView = new ImageProcessorTextView(new CorruptedAppendable());
      badView.renderImage(this.image1, "one");
      fail("Somehow could render the message");
    } catch (IOException e) {
      assertEquals("IOException thrown", e.getMessage());
    }
  }
}
