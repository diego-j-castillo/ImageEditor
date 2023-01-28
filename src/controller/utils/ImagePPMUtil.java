package controller.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import model.ImageModel;
import model.ImageRGB;

/**
 * Utility that allows for the conversion of a PPM file to an ImageRGB object and vice-versa.
 */
public class ImagePPMUtil implements ImageFileUtil {
  /**
   * Read an image file in the PPM format and returns an ImageRGB object.
   *
   * @param fileName the path of the file.
   * @return ImageRGB object that matches with the PPM file
   * @throws IllegalArgumentException invalid PPM file
   * @throws FileNotFoundException could not find the file
   */
  @Override
  public ImageRGB readFile(String fileName) {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File could not be found");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }
    //now sets up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());
    String token;
    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    //creates the 3D array of pixels
    int[][][] pixels = new int[width][height][3];
    //sets the RGB values for each pixel
    for (int h = 0; h < height; h = h + 1) {
      for (int w = 0; w < width; w = w + 1) {
        pixels[w][h][0] = sc.nextInt();
        pixels[w][h][1] = sc.nextInt();
        pixels[w][h][2] = sc.nextInt();
      }
    }
    //produces the final ImageRGB object and returns it
    return new ImageRGB(pixels, maxValue);
  }

  /**
   * Takes an Image object and makes it into a PPM file.
   *
   * @param image the image to be turned into a PPM file
   * @param path  file-path for the image we want to write to
   * @throws IllegalArgumentException can't convert Image to PPM file
   * @throws IllegalArgumentException error in properly writing to desired file
   */
  @Override
  public void writeFile(ImageModel image, String path) {
    if (image == null || image.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image does not work for PPM files");
    }
    //writes out the heading for the file
    StringBuilder data = new StringBuilder("P3" + System.lineSeparator());
    data.append(image.getWidth());
    data.append(" ");
    data.append(image.getHeight());
    data.append(System.lineSeparator());
    data.append(image.getMaxColorValue());
    data.append(System.lineSeparator());
    //puts in the details of each pixel
    for (int height = 0; height < image.getHeight(); height = height + 1) {
      for (int width = 0; width < image.getWidth(); width = width + 1) {
        data.append(image.getComponentValue(width, height, 0));
        data.append(System.lineSeparator());
        data.append(image.getComponentValue(width, height, 1));
        data.append(System.lineSeparator());
        data.append(image.getComponentValue(width, height, 2));
        data.append(System.lineSeparator());
      }
    }
    byte[] dataBytes = data.toString().getBytes();
    try {
      FileOutputStream out = new FileOutputStream(path);
      out.write(dataBytes);
      out.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not properly write to file");
    }
  }
}
