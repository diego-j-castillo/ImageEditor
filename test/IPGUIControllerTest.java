import org.junit.Before;
import org.junit.Test;

import controller.Features;
import controller.IPGUIController;
import model.IPModelGUI;
import model.IPModelGuiImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests the IPGUIController class.
 */
public class IPGUIControllerTest {
  //Examples:
  private IPModelGUI model;

  //sets to initial conditions
  @Before
  public void initial() {
    this.model = new IPModelGuiImpl();
  }

  //tests that we can go through every feature and successfully interact with the view
  @Test
  public void testController() {
    StringBuilder out = new StringBuilder();
    Features gController = new IPGUIController(this.model, new MockGUIView(out));
    //goes through and sees if we can log every feature
    gController.commandFeature("red");
    gController.saveFeature("res/guiContTest.ppm");
    gController.brightenFeature(10);
    gController.darkenFeature(10);
    gController.loadFeature("res/image1.ppm");
    gController.saveFeature("res/image1.ppm");
    gController.darkenFeature(10);
    gController.brightenFeature(10);
    gController.commandFeature("blur");
    gController.commandFeature("fajfdsajfjdafdsaofdof");
    gController.loadFeature("res/imagedafhdsakfanfdjaf.ppm");
    gController.saveFeature("res/image.badFileTypeThatIsNotSupported");
    String[] lines = out.toString().split("\n");
    assertEquals("addFeatures", lines[0]);
    assertEquals("Load a good image first!", lines[1]);
    assertEquals("Error in saving image", lines[2]);
    assertEquals("Load a good image first!", lines[3]);
    assertEquals("Load a good image first!", lines[4]);
    assertEquals("renderImage", lines[5]);
    assertEquals("renderHistogram", lines[6]);
    assertEquals("Loaded Image", lines[7]);
    assertEquals("Saved Image", lines[8]);
    assertEquals("renderImage", lines[9]);
    assertEquals("renderHistogram", lines[10]);
    assertEquals("Applied darken on image", lines[11]);
    assertEquals("renderImage", lines[12]);
    assertEquals("renderHistogram", lines[13]);
    assertEquals("Applied brighten on image", lines[14]);
    assertEquals("renderImage", lines[15]);
    assertEquals("renderHistogram", lines[16]);
    assertEquals("Applied blur on image", lines[17]);
    assertEquals("Command not supported", lines[18]);
    assertEquals("Error in loading image", lines[19]);
    assertEquals("Error in saving image", lines[20]);
  }

  //tests that we added all the commands that we should be supporting
  @Test
  public void testCommandsPresent() {
    StringBuilder out = new StringBuilder();
    Features gController = new IPGUIController(this.model, new MockGUIView(out));
    //goes through and sees if we can log each command
    gController.loadFeature("res/image1.ppm");
    gController.commandFeature("red");
    gController.commandFeature("green");
    gController.commandFeature("blue");
    gController.commandFeature("value");
    gController.commandFeature("intensity");
    gController.commandFeature("luma");
    gController.commandFeature("greyscale");
    gController.commandFeature("horizontal");
    gController.commandFeature("vertical");
    gController.commandFeature("sepia");
    gController.commandFeature("blur");
    gController.commandFeature("sharpen");
    gController.commandFeature("fajfdsajfjdafdsaofdof");
    String[] lines = out.toString().split("\n");
    //goes through and checks that we sent the right command name each time
    assertEquals("addFeatures", lines[0]);
    assertEquals("Loaded Image", lines[3]);
    assertEquals("Applied red on image", lines[6]);
    assertEquals("Applied green on image", lines[9]);
    assertEquals("Applied blue on image", lines[12]);
    assertEquals("Applied value on image", lines[15]);
    assertEquals("Applied intensity on image", lines[18]);
    assertEquals("Applied luma on image", lines[21]);
    assertEquals("Applied greyscale on image", lines[24]);
    assertEquals("Applied horizontal on image", lines[27]);
    assertEquals("Applied vertical on image", lines[30]);
    assertEquals("Applied sepia on image", lines[33]);
    assertEquals("Applied blur on image", lines[36]);
    assertEquals("Applied sharpen on image", lines[39]);
    assertEquals("Command not supported", lines[40]);
  }
}
