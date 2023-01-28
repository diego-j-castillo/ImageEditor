import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import controller.commands.Brightness;
import controller.commands.Command;
import controller.commands.Filter;
import controller.commands.Filters;
import controller.commands.HFlip;
import controller.commands.Load;
import controller.commands.MixVis;
import controller.commands.Save;
import controller.commands.Transform;
import controller.commands.Transformation;
import controller.commands.VFlip;
import model.ImageModel;
import model.ImageProcessorModel;
import model.ImageProcessorModelImpl;
import model.ImageRGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for Modifications, specifically their apply method. Also includes exception testing
 */
public class CommandTest {
  //Examples:
  private ImageModel image1;
  private ImageModel image2;
  private ImageProcessorModel model;

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
    this.image2 = new ImageRGB(pixel2, 255);
    this.model = new ImageProcessorModelImpl();
    this.model.setImageAt(this.image1, "image1");
    this.model.setImageAt(this.image2, "image2");
  }

  //tests that Modifications that rely on having the 3 RGB values only take images that have those
  //3 components
  @Test
  public void testModsNotEnoughComponentsRGB() {
    int[][][] badPixels = new int[10][20][2];
    ImageModel bad = new BadImage(badPixels, 255, 2);
    this.model.setImageAt(bad, "imageB");
    //for visualizing a component
    try {
      new Transform(Transformation.Red, "imageB", "image3").apply(this.model);
      fail("Somehow applied the mod");
    } catch (IllegalArgumentException e) {
      assertEquals("Image type must support at least 3 components", e.getMessage());
    }
    //brightness change
    try {
      new Brightness(1, true, "imageB", "image3").apply(this.model);
      fail("Somehow applied the mod");
    } catch (IllegalArgumentException e) {
      assertEquals("Image type must support at least 3 components", e.getMessage());
    }
    //mixed visualization
    try {
      new MixVis("value", "imageB", "image3").apply(this.model);
      fail("Somehow applied the mod");
    } catch (IllegalArgumentException e) {
      assertEquals("Image type must support at least 3 components", e.getMessage());
    }
    //transformation
    try {
      new Transform(Transformation.Sepia, "imageB", "image3").apply(this.model);
      fail("Somehow applied the mod");
    } catch (IllegalArgumentException e) {
      assertEquals("Image type must support at least 3 components", e.getMessage());
    }
    //filter
    try {
      new Filter(Filters.Sharpen, "imageB", "image3").apply(this.model);
      fail("Somehow applied the mod");
    } catch (IllegalArgumentException e) {
      assertEquals("Image type must support at least 3 components", e.getMessage());
    }
  }

  //tests that we can accept visualization for red properly, so all pixel components match the
  //red value for that pixel
  @Test
  public void testVisRed() {
    Command red = new Transform(Transformation.Red, "image1", "image3");
    ImageModel image3 = red.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 0), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 0), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 0), image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can accept visualization for green properly, so all pixel components match the
  //green value for that pixel
  @Test
  public void testVisGreen() {
    Command green = new Transform(Transformation.Green, "image1", "image3");
    ImageModel image3 = green.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 1), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 1), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 1), image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can accept visualization for blue properly, so all pixel components match the
  //blue value for that pixel
  @Test
  public void testVisBlue() {
    Command blue = new Transform(Transformation.Blue, "image1", "image3");
    ImageModel image3 = blue.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 2), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 2), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 2), image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can accept value visualization and convert an image
  @Test
  public void testVisValue() {
    Command vM = new MixVis("value", "image2", "image3");
    ImageModel image3 = vM.apply(this.model);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //gets the max out of the three RGB components and then checks that each component was
        //assigned to that value
        int max = Math.max(Math.max(this.image2.getComponentValue(w, h, 0),
            this.image2.getComponentValue(w, h, 1)), image2.getComponentValue(w, h, 2));
        assertEquals(max, image3.getComponentValue(w, h, 0));
        assertEquals(max, image3.getComponentValue(w, h, 1));
        assertEquals(max, image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can accept intensity visualization and convert an image
  @Test
  public void testVisIntensity() {
    Command iM = new Transform(Transformation.Intensity, "image2", "image3");
    ImageModel image3 = iM.apply(this.model);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //gets the average value of each pixel
        int avg = (this.image2.getComponentValue(w, h, 0) + this.image2.getComponentValue(w, h, 1)
            + this.image2.getComponentValue(w, h, 2)) / 3;
        assertEquals(avg, image3.getComponentValue(w, h, 0));
        assertEquals(avg, image3.getComponentValue(w, h, 1));
        assertEquals(avg, image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can accept luma visualization and convert an image
  @Test
  public void testVisLuma() {
    Command lM = new Transform(Transformation.Luma, "image2", "image3");
    ImageModel image3 = lM.apply(this.model);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //gets the luma value of each pixel
        int luma = (int) (0.2126 * this.image2.getComponentValue(w, h, 0)
            + 0.7152 * this.image2.getComponentValue(w, h, 1)
            + 0.0722 * this.image2.getComponentValue(w, h, 2));
        assertEquals(luma, image3.getComponentValue(w, h, 0));
        assertEquals(luma, image3.getComponentValue(w, h, 1));
        assertEquals(luma, image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can convert an image to sepia tone
  @Test
  public void testSepia() {
    Command sepia = new Transform(Transformation.Sepia, "image2", "image3");
    ImageModel image3 = sepia.apply(this.model);
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //gets the new values that would be for each component
        int red = (int) (0.393 * this.image2.getComponentValue(w, h, 0)
            + 0.769 * this.image2.getComponentValue(w, h, 1)
            + 0.189 * this.image2.getComponentValue(w, h, 2));
        int green = (int) (0.349 * this.image2.getComponentValue(w, h, 0)
            + 0.686 * this.image2.getComponentValue(w, h, 1)
            + 0.168 * this.image2.getComponentValue(w, h, 2));
        int blue = (int) (0.272 * this.image2.getComponentValue(w, h, 0)
            + 0.534 * this.image2.getComponentValue(w, h, 1)
            + 0.131 * this.image2.getComponentValue(w, h, 2));
        assertEquals(red, image3.getComponentValue(w, h, 0));
        assertEquals(green, image3.getComponentValue(w, h, 1));
        assertEquals(blue, image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we throw an exception when giving a null transformation
  @Test
  public void testTransformException() {
    try {
      Command nullTransform = new Transform(null, "image1", "imageT");
      fail("Somehow created transform");
    } catch (IllegalArgumentException e) {
      assertEquals("Transformation can not be null", e.getMessage());
    }
  }

  //tests the blurring feature
  @Test
  public void testBlur() {
    Command blur = new Filter(Filters.Blur, "image1", "image3");
    ImageModel image3 = blur.apply(this.model);
    //an image that is filtered using the right kernel for blurring
    this.model.filterImage("image1", new double[][]{
        {1 / 16.0, 1 / 8.0, 1 / 16.0},
        {1 / 8.0, 1 / 4.0, 1 / 8.0},
        {1 / 16.0, 1 / 8.0, 1 / 16.0}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 0), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 1), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 2), image3.getComponentValue(w, h, 2));
      }
    }
  }

  @Test
  public void testSharpen() {
    Command sharpen = new Filter(Filters.Sharpen, "image1", "image3");
    ImageModel image3 = sharpen.apply(this.model);
    //an image that has the filter applied using the kernel for sharpening
    this.model.filterImage("image1", new double[][]{
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0},
        {-1 / 8.0, 1 / 4.0, 1.0, 1 / 4.0, -1 / 8.0,},
        {-1 / 8.0, 1 / 4.0, 1 / 4.0, 1 / 4.0, -1 / 8.0,},
        {-1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0, -1 / 8.0,}
    });
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        assertEquals(this.image1.getComponentValue(w, h, 0), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, h, 1), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, h, 2), image3.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the Filter Command properly throws exceptions when constructed wrong
  @Test
  public void testFilterException() {
    try {
      Command nullFilter = new Filter(null, "image1", "imageT");
      fail("Somehow created filter");
    } catch (IllegalArgumentException e) {
      assertEquals("Filter can not be null", e.getMessage());
    }
  }

  //tests that we can horizontally flip an image
  @Test
  public void testHFlip() {
    Command hFlip = new HFlip("image1", "image3");
    ImageModel image3 = hFlip.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        //checks that the flip works for odd widths
        assertEquals(this.image1.getComponentValue(4 - w, h, 0), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(4 - w, h, 1), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(4 - w, h, 2), image3.getComponentValue(w, h, 2));
      }
    }
    Command hFlipEven = new HFlip("image2", "image4");
    hFlipEven.apply(this.model);
    ImageModel image4 = this.model.getImageAt("image4");
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //checks that the flip works for even widths
        assertEquals(this.image2.getComponentValue(3 - w, h, 0), image4.getComponentValue(w, h, 0));
        assertEquals(this.image2.getComponentValue(3 - w, h, 1), image4.getComponentValue(w, h, 1));
        assertEquals(this.image2.getComponentValue(3 - w, h, 2), image4.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can vertically flip an image
  @Test
  public void testVFlip() {
    Command vFlip = new VFlip("image1", "image3");
    ImageModel image3 = vFlip.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        //checks that the flip works for even heights
        assertEquals(this.image1.getComponentValue(w, 3 - h, 0), image3.getComponentValue(w, h, 0));
        assertEquals(this.image1.getComponentValue(w, 3 - h, 1), image3.getComponentValue(w, h, 1));
        assertEquals(this.image1.getComponentValue(w, 3 - h, 2), image3.getComponentValue(w, h, 2));
      }
    }
    Command vFlipOdd = new VFlip("image2", "image4");
    vFlipOdd.apply(this.model);
    ImageModel image4 = this.model.getImageAt("image4");
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //checks that the flip works for odd heights
        assertEquals(this.image2.getComponentValue(w, 4 - h, 0), image4.getComponentValue(w, h, 0));
        assertEquals(this.image2.getComponentValue(w, 4 - h, 1), image4.getComponentValue(w, h, 1));
        assertEquals(this.image2.getComponentValue(w, 4 - h, 2), image4.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can dim an image, and that if we go below 0, we clamp to 0
  @Test
  public void testDim() {
    Command dim = new Brightness(1, true, "image1", "image3");
    ImageModel image3 = dim.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        //gets what should be the new RGB values, clamping for 0
        int nRed = Math.max(this.image1.getComponentValue(w, h, 0) - 1, 0);
        int nGreen = Math.max(this.image1.getComponentValue(w, h, 1) - 1, 0);
        int nBlue = Math.max(this.image1.getComponentValue(w, h, 2) - 1, 0);
        assertEquals(nRed, image3.getComponentValue(w, h, 0));
        assertEquals(nGreen, image3.getComponentValue(w, h, 1));
        assertEquals(nBlue, image3.getComponentValue(w, h, 2));
      }
    }
    //dims so crazy that we go down to 0
    Command ultraDim = new Brightness(1000, true, "image2", "image4");
    ultraDim.apply(this.model);
    ImageModel image4 = this.model.getImageAt("image4");
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //all values should be 0
        assertEquals(0, image4.getComponentValue(w, h, 0));
        assertEquals(0, image4.getComponentValue(w, h, 1));
        assertEquals(0, image4.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that we can brighten an image, and that if we go above the limit, we clamp to the
  //lower limit
  @Test
  public void testBrighten() {
    Command bright = new Brightness(1, false, "image1", "image3");
    ImageModel image3 = bright.apply(this.model);
    for (int w = 0; w < 5; w = w + 1) {
      for (int h = 0; h < 4; h = h + 1) {
        //gets what should be the new RGB values, clamping for 0
        int nRed = Math.min(this.image1.getComponentValue(w, h, 0) + 1, 255);
        int nGreen = Math.min(this.image1.getComponentValue(w, h, 1) + 1, 255);
        int nBlue = Math.min(this.image1.getComponentValue(w, h, 2) + 1, 255);
        assertEquals(nRed, image3.getComponentValue(w, h, 0));
        assertEquals(nGreen, image3.getComponentValue(w, h, 1));
        assertEquals(nBlue, image3.getComponentValue(w, h, 2));
      }
    }
    //dims so crazy that we go down to 0
    Command ultraB = new Brightness(1000, false, "image2", "image4");
    ultraB.apply(this.model);
    ImageModel image4 = this.model.getImageAt("image4");
    for (int w = 0; w < 4; w = w + 1) {
      for (int h = 0; h < 5; h = h + 1) {
        //all values should be 0
        assertEquals(255, image4.getComponentValue(w, h, 0));
        assertEquals(255, image4.getComponentValue(w, h, 1));
        assertEquals(255, image4.getComponentValue(w, h, 2));
      }
    }
  }

  //tests that the Brightness command properly handles an exception when we give it a bad change
  //value
  @Test
  public void testBrightnessNegValue() {
    //giving a negative value when trying to brighten should not work
    try {
      Command badBright = new Brightness(-1, false, "image1", "iB");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not have negative change", e.getMessage());
    }
    //giving a negative value when trying to dim should not work
    try {
      Command badBright = new Brightness(-32, true, "image1", "iD");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not have negative change", e.getMessage());
    }
  }

  //tests the load command to make sure that we properly load an image from a source
  @Test
  public void testLoad() {
    //tests for ImagePPMUtil conversions
    try {
      this.model.getImageAt("image3");
      fail("Somehow got an image that did not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    Command load = new Load("res/image1.ppm", "image3");
    load.apply(this.model);
    try {
      //we can find the image now in the model
      ImageModel three = this.model.getImageAt("image3");
      //checks the aspects of the image that should be correct
      assertEquals(5, three.getWidth());
      assertEquals(4, three.getHeight());
      assertEquals(255, three.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail("Image was not loaded into model properly");
    }
    //tests for ImageGenUtil conversions
    try {
      this.model.getImageAt("image4");
      fail("Somehow got an image that did not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
    }
    Command loadGen = new Load("res/image1.png", "image4");
    loadGen.apply(this.model);
    try {
      //we can find the image now in the model
      ImageModel four = this.model.getImageAt("image4");
      //checks the aspects of the image that should be correct
      assertEquals(5, four.getWidth());
      assertEquals(4, four.getHeight());
      assertEquals(255, four.getMaxColorValue());
    } catch (IllegalArgumentException e) {
      fail("Image was not loaded into model properly");
    }
  }

  //tests for bad inputs being given to load for a specific image
  @Test
  public void testBadLoad() {
    //checks that the input file is a file by having a "." to designate the file type
    Command notFile = new Load("tomato", "image4");
    try {
      notFile.apply(this.model);
      fail("Somehow loaded a not-file");
    } catch (IllegalArgumentException e) {
      assertEquals("File was not given", e.getMessage());
    }
    //checks that out file type given is supported
    Command badType = new Load("tomato.crazyFileType", "image4");
    try {
      badType.apply(this.model);
      fail("Somehow loaded an unsupported file type");
    } catch (IllegalArgumentException e) {
      assertEquals("File type not supported", e.getMessage());
    }
    //file path does not exist and can't be found
    Command notPresent = new Load("res/fjdafWjdajfarjdjaljfaiWjfaiejlkdjlkfa.ppm", "image4");
    try {
      notPresent.apply(this.model);
      fail("Somehow loaded a non-existent file");
    } catch (IllegalArgumentException e) {
      assertEquals("File could not be found", e.getMessage());
    }
  }

  //tests that the save command will properly create an image
  @Test
  public void testSave() {
    //tests that saving with the ImagePPMUtil works
    Command save = new Save("res/saveTest.ppm", "image1");
    try {
      save.apply(this.model);
      //checks that the file save exists
      File file = new File("res/saveTest.ppm");
      assertTrue(file.exists());
      //checks that the image we got is the same in its components when converted back,
      //which means that the saving was done properly
      Command load = new Load("res/saveTest.ppm", "check");
      load.apply(this.model);
      ImageModel check = this.model.getImageAt("check");
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0), check.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1), check.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 1), check.getComponentValue(w, h, 1));
        }
      }
    } catch (IllegalStateException e) {
      fail("Could not properly save Image to file");
    }
    //tests that save works for the ImageGenUtil
    Command genS = new Save("res/saveTest.png", "image1");
    try {
      genS.apply(this.model);
      //checks that the file save exists
      File file = new File("res/saveTest.png");
      assertTrue(file.exists());
      //checks that the image we got is the same in its components when converted back,
      //which means that the saving was done properly
      Command load = new Load("res/saveTest.png", "check");
      ImageModel check = load.apply(this.model);
      for (int w = 0; w < 5; w = w + 1) {
        for (int h = 0; h < 4; h = h + 1) {
          assertEquals(this.image1.getComponentValue(w, h, 0), check.getComponentValue(w, h, 0));
          assertEquals(this.image1.getComponentValue(w, h, 1), check.getComponentValue(w, h, 1));
          assertEquals(this.image1.getComponentValue(w, h, 1), check.getComponentValue(w, h, 1));
        }
      }
    } catch (IllegalStateException e) {
      fail("Could not properly save Image to file");
    }
  }

  //tests for bad inputs being given to the save function for a specific model
  @Test
  public void testBadSave() {
    //checks that the input file is a file by having a "." to designate the file type
    Command notFile = new Save("tomato", "image4");
    try {
      notFile.apply(this.model);
      fail("Somehow loaded a not-file");
    } catch (IllegalArgumentException e) {
      assertEquals("File was not given", e.getMessage());
      //file should not exist since we could not create it
      File test = new File("tomato");
      assertFalse(test.exists());
    }
    //checks that out file type given is supported
    Command badType = new Save("tomato.crazyFileType", "image4");
    try {
      badType.apply(this.model);
      fail("Somehow loaded an unsupported file type");
    } catch (IllegalArgumentException e) {
      assertEquals("File type not supported", e.getMessage());
      //file should not exist since we could not create it
      File test = new File("tomato.crazyFileType");
      assertFalse(test.exists());
    }
    //image that we want is not in the file
    Command notPresent = new Save("res/badSaveTest.ppm", "image4");
    try {
      notPresent.apply(this.model);
      fail("Somehow loaded a non-existent file");
    } catch (IllegalArgumentException e) {
      assertEquals("Desired Image could not be found by that key", e.getMessage());
      //file should not exist since we could not create it
      File test = new File("res/badSaveTest.ppm");
      assertFalse(test.exists());
    }
  }

  //tests the getPlace() method
  @Test
  public void testGetPlace() {
    Command save = new Save("res/image1.ppm", "image1");
    assertEquals("res/image1.ppm", save.getPlace());
    Command hflip = new HFlip("base", "image1");
    assertEquals("image1", hflip.getPlace());
  }

  //we throw an exception if the string provided is not one of the greyscale operations that we
  //support (at least for now)
  @Test
  public void testMixError() {
    try {
      Command badMix = new MixVis("tomato", "image1", "imageT");
      badMix.apply(this.model);
    } catch (IllegalArgumentException e) {
      assertEquals("Visualization not supported", e.getMessage());
    }
  }

  //test for the enumeration Filters
  @Test
  public void testFiltersEnum() {
    Filters blur = Filters.Blur;
    double[][] blurMatrix = new double[][]{
        {1 / 16.0, 1 / 8.0, 1 / 16.0},
        {1 / 8.0, 1 / 4.0, 1 / 8.0},
        {1 / 16.0, 1 / 8.0, 1 / 16.0}
    };
    double[][] kernel = blur.getKernel();
    //checks that the values line up when we get the kernel
    for (int col = 0; col < 3; col = col + 1) {
      for (int row = 0; row < 3; row = row + 1) {
        assertEquals(blurMatrix[col][row], kernel[col][row], 0.001);
      }
    }
    //checks that we do not return the same reference
    assertNotEquals(kernel, blur.getKernel());
  }

  //test for the enumeration Transformation
  @Test
  public void testTransformationEnum() {
    Transformation sepia = Transformation.Sepia;
    double[][] sepiaMatrix = new double[][]{
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    };
    double[][] testM = sepia.getMatrix();
    //checks that the values line up when we get the matrix
    for (int col = 0; col < 3; col = col + 1) {
      for (int row = 0; row < 3; row = row + 1) {
        assertEquals(sepiaMatrix[col][row], testM[col][row], 0.001);
      }
    }
    //checks that we do not return the same reference
    assertNotEquals(testM, sepia.getMatrix());
  }
}
