package controller.utils;

import model.ImageModel;

/**
 * Interface that represents utility classes. Each implementation must be able to read a file type
 * and convert it to an Image object. Implementations must also be able to write to a file of the
 * type from a given Image object.
 */
public interface ImageFileUtil {
  /**
   * Read an image file of a certain type and return an adequate Image object.
   *
   * @param fileName the path of the file.
   * @return Image object that can represent the file type
   * @throws IllegalArgumentException invalid file
   * @throws IllegalArgumentException could not find the file by name given
   */
  ImageModel readFile(String fileName);

  /**
   * Takes an Image object and writes its contents to a file of a certain type.
   *
   * @param image the image to be turned into a PPM file
   * @param path  file-path for the image we want to write to
   * @throws IllegalArgumentException can't convert Image to file type
   * @throws IllegalArgumentException error in properly writing to desired file
   */
  void writeFile(ImageModel image, String path);
}
