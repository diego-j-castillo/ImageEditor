import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import model.ImageModel;
import model.ImageProcessorModelImpl;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the ImageProcessorModelImpl class.
 */
public class ImageProcessorModelImplTest {
  //Examples:
  private ImageModel image1;
  private ImageProcessorModelImpl model;

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
    this.model = new ImageProcessorModelImpl();
    this.model.setImageAt(this.image1, "image1");
    this.model.setImageAt(image2, "image2");
  }

  //tests that we can both get and set, also testing if keep getting only a copy of an image when
  //trying to get one.
  @Test
  public void testGetSet() {
    try {
      this.model.getImageAt("oneCopy");
      fail("somehow got an image");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    this.model.setImageAt(this.image1, "oneCopy");
    ImageModel copy;
    try {
      copy = this.model.getImageAt("oneCopy");
      //checks that both images have the same components, despite being stored in
      //different locations
      assertEquals(this.image1, copy);
    } catch (IllegalArgumentException e) {
      fail("Image was not found, even though we tried to set it into the collection");
    }
  }

  //tests that we can successfully remove an image from the model
  @Test
  public void testRemove() {
    try {
      this.model.getImageAt("image1");
    } catch (IllegalArgumentException e) {
      fail("Somehow file was not there to begin with");
    }
    this.model.removeImageAt("image1");
    try {
      this.model.getImageAt("image1");
      fail("Somehow file was not removed");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    //will still work if we don't have that image
    this.model.removeImageAt("image3");
  }

  //tests that the transformImage() method works as it should when given a valid matrix
  @Test
  public void testTransformImage() {
    //gets copy of old image1
    ImageModel image3 = this.image1.getImageCopy();
    //transforms the image1 in the model
    this.model.transformImage("image1",  new double[][]{
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        //gets the new values that would be for each component
        int red = (int) (0.393 * image3.getComponentValue(w, h, 0)
            + 0.769 * image3.getComponentValue(w, h, 1)
            + 0.189 * image3.getComponentValue(w, h, 2));
        int green = (int) (0.349 * image3.getComponentValue(w, h, 0)
            + 0.686 * image3.getComponentValue(w, h, 1)
            + 0.168 * image3.getComponentValue(w, h, 2));
        int blue = (int) (0.272 * image3.getComponentValue(w, h, 0)
            + 0.534 * image3.getComponentValue(w, h, 1)
            + 0.131 * image3.getComponentValue(w, h, 2));
        //checks that they line up with transformation
        assertEquals(red, this.image1.getComponentValue(w, h, 0));
        assertEquals(green, this.image1.getComponentValue(w, h, 1));
        assertEquals(blue, this.image1.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the clamping works for the transformImage() method
  @Test
  public void testTransformClamp() {
    //clamping from a lower bound
    this.model.transformImage("image1", new double[][]{
        {-10000.0, -10000.0, -10000.0},
        {-10000.0, -10000.0, -10000.0},
        {-10000.0, -10000.0, -10000.0}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(0, this.image1.getComponentValue(w, h, 0));
        assertEquals(0, this.image1.getComponentValue(w, h, 1));
        assertEquals(0, this.image1.getComponentValue(w, h, 2));
      }
    }
    //resets values
    this.initial();
    //clamping from top bound
    this.model.transformImage("image1", new double[][]{
        {10000.0, 10000.0, 10000.0},
        {10000.0, 10000.0, 10000.0},
        {10000.0, 10000.0, 10000.0}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(255, this.image1.getComponentValue(w, h, 0));
        assertEquals(255, this.image1.getComponentValue(w, h, 1));
        assertEquals(255, this.image1.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the transformImage() method only works when given a 3x3 matrix
  @Test
  public void testTransformException() {
    //both are not 3
    try {
      this.model.transformImage("image1", new double[2][2]);
      fail("somehow did the transformation");
    } catch (IllegalArgumentException e) {
      assertEquals("Transformation must use 3x3 matrix", e.getMessage());
    }
    //only one is 3
    try {
      this.model.transformImage("image1", new double[2][3]);
      fail("somehow did the transformation");
    } catch (IllegalArgumentException e) {
      assertEquals("Transformation must use 3x3 matrix", e.getMessage());
    }
    //other one is 3
    try {
      this.model.transformImage("image1", new double[3][4]);
      fail("somehow did the transformation");
    } catch (IllegalArgumentException e) {
      assertEquals("Transformation must use 3x3 matrix", e.getMessage());
    }
  }

  //tests that we can properly filter an image with a kernel
  @Test
  public void testFilterImage() {
    ImageModel image3 = this.image1.getImageCopy();
    //tests out also that we properly turn double values to ints by casting, so the value of the
    //surrounding pixels should not be added since they would be 0
    this.model.filterImage("image1", new double[][]{
        {0.0000001, 0.0000001, 0.0000001},
        {0.0000001, 2.0, 0.0000001},
        {0.0000001, 0.0000001, 0.0000001}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h  < 4; h = h + 1) {
        assertEquals(Math.min(255, 2 * image3.getComponentValue(w, h, 0)),
            this.image1.getComponentValue(w, h, 0));
        assertEquals(Math.min(255, 2 * image3.getComponentValue(w, h, 1)),
            this.image1.getComponentValue(w, h, 1));
        assertEquals(Math.min(255, 2 * image3.getComponentValue(w, h, 2)),
            this.image1.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the filterImage() works for a pixel that is on the corner of an image, not counting
  //component values for pixels that do not exist
  @Test
  public void testFilterImageCorner() {
    ImageModel image3 = this.image1.getImageCopy();
    this.model.filterImage("image1", new double[][]{
        {1 / 16.0, 1 / 8.0, 1 / 16.0},
        {1 / 8.0, 1 / 4.0, 1 / 8.0},
        {1 / 16.0, 1 / 8.0, 1 / 16.0}
    });
    //checks that we apply the filter properly, being able to exclude values that are outside the
    //image and only factoring in those that are in bounds
    int expectedRVal = (int) (1 / 8.0 * image3.getComponentValue(1, 0, 0)
        + 1 / 8.0 * image3.getComponentValue(0, 1, 0)
        + 1 / 16.0 * image3.getComponentValue(1, 1, 0)
        + 1 / 4.0 * image3.getComponentValue(0, 0, 0));
    assertEquals(expectedRVal, this.image1.getComponentValue(0, 0, 0));
    int expectedGVal = (int) (1 / 8.0 * image3.getComponentValue(1, 0, 1)
        + 1 / 8.0 * image3.getComponentValue(0, 1, 1)
        + 1 / 16.0 * image3.getComponentValue(1, 1, 1)
        + 1 / 4.0 * image3.getComponentValue(0, 0, 1));
    assertEquals(expectedGVal, this.image1.getComponentValue(0, 0, 1));
    int expectedBVal = (int) (1 / 8.0 * image3.getComponentValue(1, 0, 2)
        + 1 / 8.0 * image3.getComponentValue(0, 1, 2)
        + 1 / 16.0 * image3.getComponentValue(1, 1, 2)
        + 1 / 4.0 * image3.getComponentValue(0, 0, 2));
    assertEquals(expectedBVal, this.image1.getComponentValue(0, 0, 2));
  }

  //tests that the clamping works for the filterImage() method
  @Test
  public void testFilterClamp() {
    //clamping from a lower bound
    this.model.filterImage("image1", new double[][]{
        {-10000.0, -10000.0, -10000.0},
        {-10000.0, -10000.0, -10000.0},
        {-10000.0, -10000.0, -10000.0}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(0, this.image1.getComponentValue(w, h, 0));
        assertEquals(0, this.image1.getComponentValue(w, h, 1));
        assertEquals(0, this.image1.getComponentValue(w, h, 2));
      }
    }
    //resets values
    this.initial();
    //clamping from top bound
    this.model.filterImage("image1", new double[][]{
        {10000.0, 10000.0, 10000.0},
        {10000.0, 10000.0, 10000.0},
        {10000.0, 10000.0, 10000.0}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(255, this.image1.getComponentValue(w, h, 0));
        assertEquals(255, this.image1.getComponentValue(w, h, 1));
        assertEquals(255, this.image1.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the filterImages() works only for odd square kernels
  @Test
  public void testFilterExceptions() {
    //neither is odd
    try {
      this.model.filterImage("image1", new double[2][2]);
      fail("somehow did the filter");
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel must have odd matching dimensions", e.getMessage());
    }
    //only one is odd
    try {
      this.model.filterImage("image1", new double[3][4]);
      fail("somehow did the filter");
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel must have odd matching dimensions", e.getMessage());
    }
  }
}
