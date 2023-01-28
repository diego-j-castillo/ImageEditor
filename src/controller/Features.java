package controller;

/**
 * The features that should be supported by a GUI controller for the Processor. Includes the ability
 * to load and save images and process commands.
 */
public interface Features {
  /**
   * Loads an image from the specified location.
   *
   * @param path where the image is located
   */
  void loadFeature(String path);

  /**
   * Saves an image in hte specified location.
   *
   * @param path where the image is located
   */
  void saveFeature(String path);

  /**
   * Does a command that does not require user input.
   *
   * @param command name of the command as a string that we can look up
   */
  void commandFeature(String command);

  /**
   * Brightens the image based on the number given.
   *
   * @param increment the amount to brighten by
   */
  void brightenFeature(int increment);

  /**
   * Darkens the image based on the number given.
   *
   * @param increment the amount to darken by
   */
  void darkenFeature(int increment);
}
