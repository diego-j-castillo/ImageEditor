package controller.commands;

import java.util.HashMap;
import java.util.Map;

import controller.utils.ImageFileUtil;
import controller.utils.ImageGenUtil;
import controller.utils.ImagePPMUtil;
import model.ImageModel;
import model.ImageProcessorModel;

/**
 * Allows for the user to load images.
 */
public class Load extends ACommand {
  //known file type conversions
  private final Map<String, ImageFileUtil> knownConversions;

  /**
   * Constructs with the given source and placement keys.
   *
   * @param source name for the source file
   * @param place  key for the new image
   */
  public Load(String source, String place) {
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
   * Loads an image into the collection.
   *
   * @param images collection of images to look through
   * @return the final image after all modifications
   * @throws IllegalArgumentException file was not given as a location to load from
   * @throws IllegalArgumentException file type is not supported
   * @throws IllegalArgumentException file was not found to be loaded
   */
  @Override
  public ImageModel apply(ImageProcessorModel images) {
    //checks that if we have a file given (which must have .type)
    int dotPos = this.source.lastIndexOf(".");
    if (dotPos == -1) {
      throw new IllegalArgumentException("File was not given");
    }
    //checks if the file type is currently supported
    String fileType = this.source.substring(dotPos);
    ImageFileUtil util = this.knownConversions.getOrDefault(fileType, null);
    if (util == null) {
      throw new IllegalArgumentException("File type not supported");
    }
    //tries to convert using the respective utility, failing if the file was not found
    ImageModel loaded;
    try {
      loaded = util.readFile(this.source);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("File could not be found");
    }
    //adds the image and returns it
    images.setImageAt(loaded, this.place);
    return images.getImageAt(this.place);
  }
}
