package controller;

/**
 * Interface for the controller for the image processor. Contains all the operations that should
 * be supported by an implementation of the controller. Should not only allow for the user to
 * directly change images but to output the images and render errors/helpful messages to a view.
 */
public interface ImageProcessorController {
  /**
   * Allows for images in a model ta be modified and to have commands carried out on it.
   * Implementations should also output images and messages to a view.
   *
   * @throws IllegalStateException could not properly output to destination
   */
  void modifyImages();
}
