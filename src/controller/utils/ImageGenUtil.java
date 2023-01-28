package controller.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import model.ImageModel;
import model.ImageRGB;

/**
 * Utility that allows for the conversion of an image file to an ImageRGB object and vice-versa.
 * Supported image types are PNG, JPEG, JPG, BMP.
 */
public class ImageGenUtil implements ImageFileUtil {
  /**
   * Read one of the 5 image files and return an ImageRGB object.
   *
   * @param fileName the path of the file
   * @return ImageRGB that matches with the read file
   * @throws IllegalArgumentException invalid file
   * @throws IllegalArgumentException could not find the file by name given
   */
  @Override
  public ImageModel readFile(String fileName) {
    //checks that we support given file's type
    String type = fileName.substring(fileName.lastIndexOf("."));
    if (!type.equals(".png") && !type.equals(".jpg")
        && !type.equals(".jpeg") && !type.equals(".bmp")) {
      throw new IllegalArgumentException("File type is not supported");
    }
    //checks that the image exists and can be read
    BufferedImage source;
    try {
      source = ImageIO.read(new FileImageInputStream(new File(fileName)));
    } catch (IOException e) {
      throw new IllegalArgumentException("File could not be found or read");
    }
    //creates the pixel array
    int[][][] pixels = new int[source.getWidth()][source.getHeight()][3];
    for (int w = 0; w < source.getWidth(); w = w + 1) {
      for (int h = 0; h < source.getHeight(); h = h + 1) {
        //gets the RGB values of a pixel
        pixels[w][h][0] = new Color(source.getRGB(w, h)).getRed();
        pixels[w][h][1] = new Color(source.getRGB(w, h)).getGreen();
        pixels[w][h][2] = new Color(source.getRGB(w, h)).getBlue();
      }
    }
    //uses 255 as that is the default for these image types
    return new ImageRGB(pixels, 255);
  }

  /**
   * Takes an Image object and writes its contents to a file of a certain type. Casts the image
   * values when converting to 255 scale for each component.
   *
   * @param image the image to be turned into a PPM file
   * @param path  file-path for the image we want to write to
   * @throws IllegalArgumentException can't convert Image to file type
   * @throws IllegalArgumentException error in properly writing to desired file
   */
  @Override
  public void writeFile(ImageModel image, String path) {
    //check that we have enough components for conversion
    if (image == null || image.getNumComponents() < 3) {
      throw new IllegalArgumentException("Image does not work for this file type");
    }
    //the bufferedImage version of the ImageModel object we are trying to convert
    BufferedImage end = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    //goes through the ImageModel and puts data of each pixel to the BufferedImage
    for (int w = 0; w < image.getWidth(); w = w + 1) {
      for (int h = 0; h < image.getHeight(); h = h + 1) {
        //fits the values so that it is in a 255 scale
        int red = (int) (image.getComponentValue(w, h, 0) / (image.getMaxColorValue() / 255.0));
        int green = (int) (image.getComponentValue(w, h, 1) / (image.getMaxColorValue() / 255.0));
        int blue = (int) (image.getComponentValue(w, h, 2) / (image.getMaxColorValue() / 255.0));
        end.setRGB(w, h, new Color(red, green, blue).getRGB());
      }
    }
    //writes to a file using the ImageIO, catching IOExceptions if found
    try {
      ImageOutputStream out = new FileImageOutputStream(new File(path));
      //writes the file in the specific format type
      ImageIO.write(end, path.substring(path.lastIndexOf(".") + 1), out);
      out.close();
    } catch (IOException e) {
      //means that we could not find the file or that we could not support the file type
      throw new IllegalArgumentException("Could not properly write to file");
    }
  }
}
