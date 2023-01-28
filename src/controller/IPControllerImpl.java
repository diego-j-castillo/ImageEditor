package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

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
import view.ImageProcessorView;

/**
 * Controller implementation for an image processor. Should allow the user to apply multiple
 * different modifications to an image and save/load images to modify.
 */
public class IPControllerImpl implements ImageProcessorController {
  //the images that we can modify
  private final ImageProcessorModel images;
  //view that allows us to output things to a destination
  private final ImageProcessorView view;
  //object from which we take input from
  private final Readable in;
  //list of all modifications that we can make on an image
  private final Map<String, Function<Scanner, Command>> knownMods;
  //tells us if the program is running or not
  private boolean isRunning;

  /**
   * Constructs a controller using a given input object to read from.
   *
   * @param images map of images
   * @param view   way that we view the images
   * @param in     object from which input is read from
   * @throws IllegalArgumentException given parameters can not be null
   */
  public IPControllerImpl(ImageProcessorModel images, ImageProcessorView view, Readable in) {
    //exception if any of the inputs are null
    if (images == null || view == null || in == null) {
      throw new IllegalArgumentException("Given input source can not be null");
    }
    //sets the model and view
    this.images = images;
    this.view = view;
    this.in = in;
    //we are not running until we do modifyImage(), so this is set to false at the start
    this.isRunning = false;
    //adds all the known commands to the list (can be expanded as needed)
    this.knownMods = new HashMap<String, Function<Scanner, Command>>();
    //commands to get the individual RGB components in greyscale
    this.knownMods.put("red-component", s -> new Transform(Transformation.Red,
        s.next(), s.next()));
    this.knownMods.put("green-component", s -> new Transform(Transformation.Green,
        s.next(), s.next()));
    this.knownMods.put("blue-component", s -> new Transform(Transformation.Blue,
        s.next(), s.next()));
    //other forms of getting greyscale
    this.knownMods.put("value-component", s -> new MixVis("value", s.next(), s.next()));
    this.knownMods.put("intensity-component", s -> new Transform(Transformation.Intensity,
        s.next(), s.next()));
    this.knownMods.put("luma-component", s -> new Transform(Transformation.Luma,
        s.next(), s.next()));
    this.knownMods.put("greyscale", s -> new Transform(Transformation.Luma,
        s.next(), s.next()));
    //linear color transformations that are not just greyscale
    this.knownMods.put("sepia", s -> new Transform(Transformation.Sepia,
        s.next(), s.next()));
    //flipping the image across both axes
    this.knownMods.put("horizontal-flip", s -> new HFlip(s.next(), s.next()));
    this.knownMods.put("vertical-flip", s -> new VFlip(s.next(), s.next()));
    //brightening and darkening an image
    this.knownMods.put("brighten", s -> new Brightness(s.nextInt(), false, s.next(), s.next()));
    this.knownMods.put("darken", s -> new Brightness(s.nextInt(), true, s.next(), s.next()));
    //filtering effects on images
    this.knownMods.put("blur", s -> new Filter(Filters.Blur, s.next(), s.next()));
    this.knownMods.put("sharpen", s -> new Filter(Filters.Sharpen, s.next(), s.next()));
    //loading and saving commands for an image
    this.knownMods.put("load", s -> new Load(s.next(), s.next()));
    this.knownMods.put("save", s -> new Save(s.next(), s.next()));
  }

  /**
   * Allows for images in a model ta be modified and to have commands carried out on it. Outputs
   * the images that are modified and created and messages about the inputs given to the view.
   * End of the method occurs only when inputs run out or when the user manually quits by inputting
   * 'quit'.
   *
   * @throws IllegalStateException could not properly output to destination
   */
  @Override
  public void modifyImages() {
    //scanner from which we read inputs from
    Scanner sc = new Scanner(this.in);
    //boolean value that allows us to quit the game in manual mode
    this.isRunning = true;
    //keeps going until we run out of commands or have quit manually
    while (this.isRunning && sc.hasNext()) {
      try {
        String input = sc.next();
        this.execute(input, sc);
      } catch (NoSuchElementException e) {
        //means that we could not read input, or we ran out of elements while giving a command
        this.transmitMessage("Inputs are invalid");
      }
    }
    //tells that the running of the method has ended
    this.transmitMessage("Program ended");
  }

  /**
   * Carries out a command if it is known by the controller.
   *
   * @param input the name of the potential command
   * @param sc    scanner object to read from
   * @throws IllegalStateException could not transmit to output properly
   */
  private void execute(String input, Scanner sc) {
    //this input means we quit the game
    if (input.equals("quit")) {
      this.isRunning = false;
    } else {
      //checks that we were given a valid command
      Function<Scanner, Command> mod = this.knownMods.getOrDefault(input, null);
      //if we got null, we got an invalid command, so we transmit that
      if (mod == null) {
        this.transmitMessage("Invalid command");
      } else {
        //we try to apply the command since the controller knows it
        try {
          Command m = mod.apply(sc);
          //renders out the image using it and the key, since exception was not thrown
          this.transmitImage(m.apply(this.images), m.getPlace());
        } catch (IllegalArgumentException e) {
          //error in user input, so we tell the user the error through the view
          this.transmitMessage(e.getMessage());
        }
      }
    }
  }

  /**
   * Transmits a message to the view's output destination.
   *
   * @param image image to be output
   * @param key   string that the image is known as or referred to
   * @throws IllegalStateException output could not be properly transmitted
   */
  private void transmitImage(ImageModel image, String key) {
    try {
      this.view.renderImage(image, key);
    } catch (IOException e) {
      throw new IllegalStateException("Output could not be transmitted");
    }
  }

  /**
   * Transmits a message to the view's output destination.
   *
   * @param msg message to be thrown
   * @throws IllegalStateException output could not be properly transmitted
   */
  private void transmitMessage(String msg) {
    try {
      this.view.renderMessage(msg);
    } catch (IOException e) {
      throw new IllegalStateException("Output could not be transmitted");
    }
  }
}
