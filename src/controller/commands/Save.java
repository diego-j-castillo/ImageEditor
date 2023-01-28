package controller.commands;

import java.util.HashMap;
import java.util.Map;

import controller.utils.ImageFileUtil;
import controller.utils.ImageGenUtil;
import controller.utils.ImagePPMUtil;
import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Allows for the user to save images.
 */
public class Save extends ACommand {
  //known file type conversions
  private final Map<String, ImageFileUtil> knownConversions;

  /**
   * Constructs with the given source and placement keys.
   *
   * @param place  key for the image to save
   * @param source destination for the image to save
   */
  public Save(String place, String source) {
    super(source, place);
    //adds all the conversions that we know so far
    this.knownConversions = new HashMap<String, ImageFileUtil>();
    this.knownConversions.put(".ppm", new ImagePPMUtil());
    this.knownConversions.put(".jpeg", new ImageGenUtil());
    this.knownConversions.put(".jpg", new ImageGenUtil());
    this.knownConversions.put(".png", new ImageGenUtil());
    this.knownConversions.put(".bmp", new ImageGenUtil());
  }

  /**
   * Saves the image to the specified location.
   *
   * @param images collection of images to look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException file was not given as a location to save to
   * @throws IllegalArgumentException file type is not supported
   * @throws IllegalArgumentException file was nt able to be saved to
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //checks if the given file path leads to a file (ends in .type)
    int dotPos = this.place.lastIndexOf(".");
    if (dotPos == -1) {
      throw new IllegalArgumentException("File was not given");
    }
    //checks if we support this file type
    String fileType = this.place.substring(dotPos);
    ImageFileUtil util = this.knownConversions.getOrDefault(fileType, null);
    if (util == null) {
      throw new IllegalArgumentException("File type not supported");
    }
    //saves the file to the location using the utility and returns it
    util.writeFile(images.getImageAt(this.source), this.place);
    return images.getImageAt(source);
  }
}
