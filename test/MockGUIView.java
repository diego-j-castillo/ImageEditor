import java.awt.Image;

import controller.Features;
import view.ImageProcessorGUIView;

/**
 * A mock gui View that simply records all the operations of a controller. Used to test that the
 * controller works for all of its inputs.
 */
public class MockGUIView implements ImageProcessorGUIView {
  //log of all the actions performed
  StringBuilder log;

  /**
   * Constructs a mock view with a StringBuilder to record the output.
   *
   * @param log the record of all inputs from the controller
   */
  public MockGUIView(StringBuilder log) {
    this.log = log;
  }

  /**
   * Logs that this method was called.
   *
   * @param image the image that is to be visualized
   */
  @Override
  public void renderImage(Image image) {
    this.log.append("renderImage\n");
  }

  /**
   * Logs that this method was called.
   *
   * @param msg the message that we are to render out
   */
  @Override
  public void renderMessage(String msg) {
    this.log.append(msg);
    this.log.append("\n");
  }

  /**
   * Logs that this method was called.
   *
   * @param histogram an array that represents the values in the histogram
   */
  @Override
  public void renderHistogram(int[][] histogram) {
    this.log.append("renderHistogram\n");
  }

  /**
   * Logs that this method was called.
   *
   * @param features what we assign actions or keys to
   */
  @Override
  public void addFeatures(Features features) {
    this.log.append("addFeatures\n");
  }
}
