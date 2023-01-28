package controller;

import java.util.HashMap;
import java.util.Map;

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
import model.IPModelGUI;
import view.ImageProcessorGUIView;

/**
 * Controller that works with a GUI to allow for image manipulation. This controller only keeps
 * track of the most recent image and none else. Any modifications done are on that image. Uses the
 * key "current" as the location for the image that is currently being modified.
 */
public class IPGUIController implements Features {
  //Model that we use to store the images
  private final IPModelGUI model;
  //GUI view that we use
  private final ImageProcessorGUIView view;
  //list of all modifications that we can make on an image
  private final Map<String, Command> knownMods;

  /**
   * Constructs a basic controller.
   *
   * @param model the model that we are to use that must be able to produce a histogram and Images
   * @param view a view that supports GUI
   * @throws IllegalArgumentException either model or view are null
   */
  public IPGUIController(IPModelGUI model, ImageProcessorGUIView view) {
    //checks that the givens are not null
    if (model == null || view == null) {
      throw new IllegalArgumentException("Given model and view can not be null");
    }
    this.model = model;
    this.view = view;
    //adds the known commands, but not brighten and darken since those two need additional input
    this.knownMods = new HashMap<>();
    this.knownMods.put("red", new Transform(Transformation.Red, "current", "current"));
    this.knownMods.put("blue", new Transform(Transformation.Blue, "current", "current"));
    this.knownMods.put("green", new Transform(Transformation.Green, "current", "current"));
    this.knownMods.put("value", new MixVis("value", "current", "current"));
    this.knownMods.put("intensity", new Transform(Transformation.Intensity, "current", "current"));
    this.knownMods.put("luma", new Transform(Transformation.Luma, "current", "current"));
    this.knownMods.put("greyscale", new Transform(Transformation.Luma, "current", "current"));
    this.knownMods.put("horizontal", new HFlip("current", "current"));
    this.knownMods.put("vertical", new VFlip("current", "current"));
    this.knownMods.put("sepia", new Transform(Transformation.Sepia, "current", "current"));
    this.knownMods.put("blur", new Filter(Filters.Blur, "current", "current"));
    this.knownMods.put("sharpen", new Filter(Filters.Sharpen, "current", "current"));
    //adds the features to the view so that we can interact through the GUI
    this.view.addFeatures(this);
  }

  /**
   * Loads an image from the specified location.
   *
   * @param path where the image is located
   */
  @Override
  public void loadFeature(String path) {
    try {
      new Load(path, "current").apply(this.model);
      this.view.renderImage(this.model.convertImage("current"));
      this.view.renderHistogram(this.model.getHistogram("current"));
      this.view.renderMessage("Loaded Image");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Error in loading image");
    }
  }

  /**
   * Saves an image in hte specified location.
   *
   * @param path where the image is located
   */
  @Override
  public void saveFeature(String path) {
    try {
      new Save(path, "current").apply(this.model);
      this.view.renderMessage("Saved Image");
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Error in saving image");
    }
  }

  /**
   * Does a command that does not require user input.
   *
   * @param command name of the command as a string that we can look up
   */
  @Override
  public void commandFeature(String command) {
    Command com = this.knownMods.getOrDefault(command, null);
    if (com == null) {
      this.view.renderMessage("Command not supported");
    } else {
      try {
        com.apply(this.model);
        this.view.renderImage(this.model.convertImage("current"));
        this.view.renderHistogram(this.model.getHistogram("current"));
        this.view.renderMessage(String.format("Applied %s on image", command));
      } catch (IllegalArgumentException e) {
        this.view.renderMessage("Load a good image first!");
      }
    }
  }

  /**
   * Brightens the image based on the number given.
   *
   * @param increment the amount to brighten by
   */
  @Override
  public void brightenFeature(int increment) {
    Command bri = new Brightness(increment, false, "current", "current");
    try {
      bri.apply(this.model);
      this.view.renderImage(this.model.convertImage("current"));
      this.view.renderHistogram(this.model.getHistogram("current"));
      this.view.renderMessage(String.format("Applied %s on image", "brighten"));
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Load a good image first!");
    }
  }

  /**
   * Darkens the image based on the number given.
   *
   * @param increment the amount to darken by
   */
  @Override
  public void darkenFeature(int increment) {
    Command dark = new Brightness(increment, true, "current", "current");
    try {
      dark.apply(this.model);
      this.view.renderImage(this.model.convertImage("current"));
      this.view.renderHistogram(this.model.getHistogram("current"));
      this.view.renderMessage(String.format("Applied %s on image", "darken"));
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Load a good image first!");
    }
  }
}
