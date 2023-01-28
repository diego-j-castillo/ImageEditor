import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Random;

import controller.IPControllerImpl;
import controller.ImageProcessorController;
import model.ImageModel;
import model.ImageProcessorModel;
import model.ImageProcessorModelImpl;
import model.ImageRGB;
import view.ImageProcessorTextView;
import view.ImageProcessorView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the IPControllerImpl class.
 */
public class IPControllerImplTest {
  private ImageModel image1;
  private ImageModel image2;
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
    this.image2 = new ImageRGB(pixel2, 255);
    //sets up the view
    this.out = new StringBuilder();
    this.view = new ImageProcessorTextView(this.out);
  }

  //tests that the modification commands can be processed
  @Test
  public void testCommands() {
    //sets the commands that we want to test
    StringReader in = new StringReader("red-component image1 redOne "
        + "green-component image1 greenOne "
        + "blue-component image2 blueTwo "
        + "value-component image2 valueTwo "
        + "intensity-component image1 intensityOne "
        + "luma-component image1 lumaOne "
        + "horizontal-flip image2 horTwo "
        + "vertical-flip horTwo verHorTwo "
        + "brighten 50 image1 brightOne "
        + "darken 50 image1 dimOne"
        + " save res/image1.ppm image1"
        + " save res/image2.ppm image2"
        + " load res/image1.ppm one2"
        + " save res/oneCopy.ppm one2"
        //invalid commands
        + " lettuce beef cromulent"
        //new commands
        + " blur image1 blurOne"
        + " sharpen image2 sharpTwo"
        + " greyscale image1 greyOne"
        + " sepia image2 sepiaTwo"
        + " quit load res/image2.ppm one3");
    //sets the group of images
    ImageProcessorModel model = new ImageProcessorModelImpl();
    model.setImageAt(this.image1, "image1");
    model.setImageAt(this.image2, "image2");
    //creates the controller to modify images
    ImageProcessorController controller = new IPControllerImpl(model, this.view, in);
    controller.modifyImages();
    String[] lines = out.toString().split("\n");
    assertEquals("Image of size (5, 4) stored as redOne", lines[0]);
    assertEquals("Image of size (5, 4) stored as greenOne", lines[1]);
    assertEquals("Image of size (4, 5) stored as blueTwo", lines[2]);
    assertEquals("Image of size (4, 5) stored as valueTwo", lines[3]);
    assertEquals("Image of size (5, 4) stored as intensityOne", lines[4]);
    assertEquals("Image of size (5, 4) stored as lumaOne", lines[5]);
    assertEquals("Image of size (4, 5) stored as horTwo", lines[6]);
    assertEquals("Image of size (4, 5) stored as verHorTwo", lines[7]);
    assertEquals("Image of size (5, 4) stored as brightOne", lines[8]);
    assertEquals("Image of size (5, 4) stored as dimOne", lines[9]);
    assertEquals("Image of size (5, 4) stored as res/image1.ppm", lines[10]);
    assertEquals("Image of size (4, 5) stored as res/image2.ppm", lines[11]);
    assertEquals("Image of size (5, 4) stored as one2", lines[12]);
    assertEquals("Image of size (5, 4) stored as res/oneCopy.ppm", lines[13]);
    assertEquals("Invalid command", lines[14]);
    assertEquals("Invalid command", lines[15]);
    assertEquals("Invalid command", lines[16]);
    assertEquals("Image of size (5, 4) stored as blurOne", lines[17]);
    assertEquals("Image of size (4, 5) stored as sharpTwo", lines[18]);
    assertEquals("Image of size (5, 4) stored as greyOne", lines[19]);
    assertEquals("Image of size (4, 5) stored as sepiaTwo", lines[20]);
    assertEquals("Program ended", lines[21]);
  }

  //tests that we can handle exceptions thrown by methods properly
  @Test
  public void testHandleBadInput() {
    StringReader in = new StringReader(
        //bad save location and image type for PPMs
        "save /\\*?<>.ppm bImage "
            //image does not exist
            + "save epic.ppm dajfodsafodsafodaoifda "
            //file could not be found
            + "load lettuceEaterWeeHOooooooo.ppm image3 "
            //unsupported file type for load
            + "load res/image.rngssaa image1 "
            //unsupported file type for save
            + "save res/image.crazyFIleTypeThatDoesNotExist image1 "
            //saving non file
            + "save tomato image2 "
            //loading non file
            + "save tomato image2 "
            //red component of bad image object
            + "red-component bImage image2 "
            //green component of bad image object
            + "green-component bImage image2 "
            //blue component of bad image object
            + "blue-component bImage image2 "
            //value component of bad image object
            + "value-component bImage image2 "
            //intensity component of bad image object
            + "intensity-component bImage image2 "
            //luma component of bad image object
            + "luma-component bImage image2 "
            //negative number for brightening
            + "brighten -1 image1 imageB "
            //negative number for dimming
            + "darken -1 image1 imageD "
            //nonnumber for dimming
            + "darken lettuce image1 imageD "
            //nonnumber for brightening
            + "brighten lettuce image1 imageB "
            //runs out of inputs
            + "darken "
            //greyscale of bad image object
            + "greyscale bImage image2 "
            //sepia of bad image object
            + "sepia bImage image2 "
            //blur of bad image object
            + "blur bImage image2 "
            //sharpen of bad image object
            + "sharpen bImage image2 ");
    ImageProcessorModel images = new ImageProcessorModelImpl();
    images.setImageAt(new BadImage(new int[10][20][2], 255, 2), "bImage");
    images.setImageAt(this.image1, "image1");
    //creates the controller to modify images
    ImageProcessorController controller = new IPControllerImpl(images, this.view, in);
    controller.modifyImages();
    String[] lines = out.toString().split("\n");
    assertEquals("Image does not work for PPM files", lines[0]);
    assertEquals("Desired Image could not be found by that key", lines[1]);
    assertEquals("File could not be found", lines[2]);
    assertEquals("File type not supported", lines[3]);
    assertEquals("File type not supported", lines[4]);
    assertEquals("File was not given", lines[5]);
    assertEquals("File was not given", lines[6]);
    assertEquals("Image type must support at least 3 components", lines[7]);
    assertEquals("Image type must support at least 3 components", lines[8]);
    assertEquals("Image type must support at least 3 components", lines[9]);
    assertEquals("Image type must support at least 3 components", lines[10]);
    assertEquals("Image type must support at least 3 components", lines[11]);
    assertEquals("Image type must support at least 3 components", lines[12]);
    assertEquals("Can not have negative change", lines[13]);
    assertEquals("Can not have negative change", lines[14]);
    assertEquals("Inputs are invalid", lines[15]);
    assertEquals("Invalid command", lines[16]);
    assertEquals("Invalid command", lines[17]);
    assertEquals("Invalid command", lines[18]);
    assertEquals("Inputs are invalid", lines[19]);
    assertEquals("Invalid command", lines[20]);
    assertEquals("Invalid command", lines[21]);
    assertEquals("Invalid command", lines[22]);
    assertEquals("Inputs are invalid", lines[23]);
    assertEquals("Image type must support at least 3 components", lines[24]);
    assertEquals("Image type must support at least 3 components", lines[25]);
    assertEquals("Image type must support at least 3 components", lines[26]);
    assertEquals("Image type must support at least 3 components", lines[27]);
    assertEquals("Program ended", lines[28]);
  }

  //tests that we can handle a corrupted input
  @Test
  public void testCorruptedReadable() {
    ImageProcessorModel images = new ImageProcessorModelImpl();
    images.setImageAt(this.image1, "image1");
    ImageProcessorController controller = new IPControllerImpl(
        images, this.view, new CorruptedReadable());
    controller.modifyImages();
    assertEquals("Program ended\n", out.toString());
  }

  //tests that we can handle a corrupted output
  @Test
  public void testCorruptedAppendable() {
    ImageProcessorModel images = new ImageProcessorModelImpl();
    images.setImageAt(this.image1, "image1");
    ImageProcessorController controller = new IPControllerImpl(
        images, new ImageProcessorTextView(new CorruptedAppendable()),
        new StringReader("red-component image1 image2"));
    try {
      controller.modifyImages();
    } catch (IllegalStateException e) {
      assertEquals("Output could not be transmitted", e.getMessage());
    }
  }
}
