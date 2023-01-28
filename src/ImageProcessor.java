import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import controller.Features;
import controller.IPControllerImpl;
import controller.IPGUIController;
import controller.ImageProcessorController;
import model.IPModelGuiImpl;
import model.ImageProcessorModelImpl;
import view.ImageProcessorSwingView;
import view.ImageProcessorTextView;

/**
 * Program that allows us to run a basic text-based image processor.
 */
public class ImageProcessor {
  /**
   * Allows us to run our program using the given arguments. Options include "-script-file" to
   * run a series of commands from a file, "-script-text" to run commands from the arguments, and
   * "-manual" for the user to
   *
   * @param args arguments that we feed to the method
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      Features controllerGUI = new IPGUIController(new IPModelGuiImpl(),
          new ImageProcessorSwingView("Image Processor"));
    } else {
      ImageProcessorController controller;
      switch (args[0]) {
        //allows the user to run a script from a file
        case "-file":
          try {
            Reader inputFile = new FileReader(args[1]);
            controller = new IPControllerImpl(new ImageProcessorModelImpl(),
                new ImageProcessorTextView(System.out), inputFile);
            //starts the processor with the desired control method
            controller.modifyImages();
          } catch (FileNotFoundException e) {
            System.out.println("Could not find file with given filename");
          }
          break;
        // allows for manual control through keyboard input when we have bad arguments
        case "-text":
          controller = new IPControllerImpl(new ImageProcessorModelImpl(),
              new ImageProcessorTextView(System.out), new InputStreamReader(System.in));
          //starts the processor with the desired control method
          controller.modifyImages();
          break;
        //invalid command line input
        default:
          System.out.println("Invalid Argument");
      }
    }
  }
}
