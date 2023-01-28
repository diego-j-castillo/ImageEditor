import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import controller.utils.ImageFileUtil;
import controller.utils.ImagePPMUtil;
import model.ImageModel;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the ImagePPMUtil class.
 */
public class ImagePPMUtilTest {
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

  //tests the ability of the readFile() method to read a file from a given location
  @Test
  public void testReadFile() {
    ImageFileUtil ppmRead = new ImagePPMUtil();
    try {
      ImageModel readImage = ppmRead.readFile("res/image1.ppm");
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
  }

  //tests that the readFile() method throws the right exception when the file does not exist and
  //when the file is not of the right format
  @Test
  public void testBadRead() {
    ImageFileUtil badRead = new ImagePPMUtil();
    try {
      //file should not exist
      badRead.readFile("res/afdafsaifeajreafodajfjdaofjaoejfad.ppm");
      fail("Somehow read a file that does not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("File could not be found", e.getMessage());
    }
    try {
      //file exists but is not formatted like a PPM of P3
      badRead.readFile("res/sampleInput.txt");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid PPM file: plain RAW file should begin with P3", e.getMessage());
    }
  }

  //tests that the writeFile() method works properly when given the right input
  @Test
  public void testWriteFile() {
    ImageFileUtil saver = new ImagePPMUtil();
    try {
      saver.writeFile(this.image1, "res/writePPMTest.ppm");
      //checks if the file exists
      File check = new File("res/writePPMTest.ppm");
      assertTrue(check.exists());
      //checks that if we convert back to Image, the image made is identical, which means that
      //saving not only happens but works correctly
      ImageModel copy = saver.readFile("res/writePPMTest.ppm");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0), copy.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1), copy.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 2), copy.getComponentValue(w, h, 2));
        }
      }
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
  }

  //tests that the readFile() method throws the right exception when the Image object is not
  //supported for PPM conversion and the file could not be written properly
  @Test
  public void testBadWrite() {
    ImageFileUtil badWrite = new ImagePPMUtil();
    try {
      ImageModel badImage = new BadImage(new int[10][20][2], 255, 2);
      //image type can not be supported to convert to PPM
      badWrite.writeFile(badImage, "res/badWriteTest.ppm");
      fail("somehow created the PPM file");
    } catch (IllegalArgumentException e) {
      assertEquals("Image does not work for PPM files", e.getMessage());
      //also checks that the file was not created
      File check = new File("res/badWriteTest.ppm");
      assertFalse(check.exists());
    }
    try {
      //error in writing to file
      badWrite.writeFile(this.image1, "res/<>?\\/");
      fail("somehow created the PPM file");
    } catch (IllegalArgumentException e) {
      assertEquals("Could not properly write to file", e.getMessage());
    }
  }
}
