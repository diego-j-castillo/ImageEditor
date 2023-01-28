package controller.commands;

/**
 * Abstract class for commands that implements common aspects of them, like the need to specify
 * a source and destination image for a model.
 */
public abstract class ACommand implements Command {
  //source image key
  protected final String source;
  //new image key (what we will store it as in the model)
  protected final String place;

  /**
   * Abstract constructor that stores the source and key for a command as Strings.
   *
   * @param source where the starting image is located
   * @param place where the new image is located
   */
  public ACommand(String source, String place) {
    this.source = source;
    this.place = place;
  }

  /**
   * Returns the location of the newly created image by its key.
   *
   * @return the key for the image to be created
   */
  @Override
  public String getPlace() {
    return this.place;
  }
}
