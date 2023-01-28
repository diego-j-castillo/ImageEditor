import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import controller.utils.ImageFileUtil;
import controller.utils.ImageGenUtil;
import model.ImageModel;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the ImageGenUtil class.
 */
public class ImageGenUtilTest {
  ImageModel image1;

  @Before
  public void initial() {
    //constructs image that we are trying to read/write in these tests
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

  //tests the ability of the readFile() method to read a file from a given location. All the
  //images that we read from are the original PPM image in their respective formats, so
  //by checking the components, we can make sure that load makes an accurate ImageModel
  @Test
  public void testReadFile() {
    ImageFileUtil genRead = new ImageGenUtil();
    //tries for png files to see if we can read it accurately
    try {
      ImageModel readImage = genRead.readFile("res/image1.png");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0),
              readImage.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1),
              readImage.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 2),
              readImage.getComponentValue(w, h, 2));
        }
      }
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //tries for bmp files to see if we can read it accurately
    try {
      ImageModel readImage = genRead.readFile("res/image1.bmp");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0),
              readImage.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1),
              readImage.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 2),
              readImage.getComponentValue(w, h, 2));
        }
      }
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //tries for jpg files to see if we can read it accurately
    try {
      ImageModel readImage = genRead.readFile("res/image1.jpg");
      //due to compression issues, we can't check each component for a jpg, so we will instead check
      //the other aspects of an image that do not suffer from compression issues
      assertEquals(255, readImage.getMaxColorValue());
      assertEquals(5, readImage.getWidth());
      assertEquals(4, readImage.getHeight());
      assertEquals(3, readImage.getNumComponents());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //tries for jpeg files to see if we can read it accurately
    try {
      ImageModel readImage = genRead.readFile("res/image1.jpeg");
      //due to compression issues, we can't check each component for a jpg, so we will instead check
      //the other aspects of an image that do not suffer from compression issues
      assertEquals(255, readImage.getMaxColorValue());
      assertEquals(5, readImage.getWidth());
      assertEquals(4, readImage.getHeight());
      assertEquals(3, readImage.getNumComponents());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
  }

  //tests for the ability to properly throw exceptions when given a bad input for readFile()
  @Test
  public void testBadRead() {
    ImageFileUtil badRead = new ImageGenUtil();
    try {
      //file should not exist
      badRead.readFile("res/afdafsaifeajreafodajfjdaofjaoejfad.png");
      fail("Somehow read a file that does not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("File could not be found or read", e.getMessage());
    }
    try {
      //file exists but is not supported
      badRead.readFile("res/image1.ppm");
    } catch (IllegalArgumentException e) {
      assertEquals("File type is not supported", e.getMessage());
    }
  }

  //tests that the writeFile() method works properly when given the right input
  @Test
  public void testWriteFile() {
    ImageFileUtil saver = new ImageGenUtil();
    //does for png files
    try {
      saver.writeFile(this.image1, "res/image1.png");
      //checks if the file exists
      File check = new File("res/image1.png");
      assertTrue(check.exists());
      //checks that if we convert back to Image, the image made is identical, which means that
      //saving not only happens but works correctly
      ImageModel copy = saver.readFile("res/image1.png");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0), copy.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1), copy.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 2), copy.getComponentValue(w, h, 2));
        }
      }
      //we set all images that get loaded to have this max value
      assertEquals(255, copy.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //does for bmp files
    try {
      saver.writeFile(this.image1, "res/image1.bmp");
      //checks if the file exists
      File check = new File("res/image1.bmp");
      assertTrue(check.exists());
      //checks that if we convert back to Image, the image made is identical, which means that
      //saving not only happens but works correctly
      ImageModel copy = saver.readFile("res/image1.bmp");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0), copy.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1), copy.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 2), copy.getComponentValue(w, h, 2));
        }
      }
      //we set all images that get loaded to have this max value
      assertEquals(255, copy.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //does for jpg files
    try {
      saver.writeFile(this.image1, "res/image1.jpg");
      //checks if the file exists
      File check = new File("res/image1.jpg");
      assertTrue(check.exists());
      //due to compression issues with jpgs, we can't check the individual contents of the image
      //when we try to load it back. We will just check that the dimensions work as they should
      ImageModel copy = saver.readFile("res/image1.jpg");
      assertEquals(5, copy.getWidth());
      assertEquals(4, copy.getHeight());
      //we set all images that get loaded to have this max value
      assertEquals(255, copy.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    //does for jpeg files
    try {
      saver.writeFile(this.image1, "res/image1.jpeg");
      //checks if the file exists
      File check = new File("res/image1.jpeg");
      assertTrue(check.exists());
      //due to compression issues with jpgs, we can't check the individual contents of the image
      //when we try to load it back. We will just check that the dimensions work as they should
      ImageModel copy = saver.readFile("res/image1.jpeg");
      assertEquals(5, copy.getWidth());
      assertEquals(4, copy.getHeight());
      //we set all images that get loaded to have this max value
      assertEquals(255, copy.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
  }

  //tests to see if exceptions are thrown correctly when bad input is given to the writeFile()
  //method
  @Test
  public void testBadSave() {
    ImageFileUtil badWrite = new ImageGenUtil();
    try {
      ImageModel badImage = new BadImage(new int[10][20][2], 255, 2);
      //image type can not be supported to convert to PPM
      badWrite.writeFile(badImage, "res/badWriteTest.png");
      fail("somehow created the file");
    } catch (IllegalArgumentException e) {
      assertEquals("Image does not work for this file type", e.getMessage());
      //also checks that the file was not created
      File check = new File("res/badWriteTest.png");
      assertFalse(check.exists());
    }
    try {
      //error in writing to file
      badWrite.writeFile(this.image1, "res/<>?\\/");
      fail("somehow created the file");
    } catch (IllegalArgumentException e) {
      assertEquals("Could not properly write to file", e.getMessage());
    }
  }
}
