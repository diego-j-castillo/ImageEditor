package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import controller.Features;

/**
 * GUI interface that uses the Swing library for its functions.
 */
public class ImageProcessorSwingView extends JFrame implements ImageProcessorGUIView {
  //the label that we use to place any images we need the user to see
  private final JLabel displayLabel;
  //the label that we use to place the histogram
  private final JLabel histoLabel;
  //label that prints out error and action messages to the user, so that they get feedback
  private final JLabel message;
  //buttons to load and save an image
  private final JButton loadButton;
  private final JButton saveButton;
  //buttons for all of hte individual commands that we want to support
  private final JButton redButton;
  private final JButton greenButton;
  private final JButton blueButton;
  private final JButton horButton;
  private final JButton verButton;
  private final JButton lumaButton;
  private final JButton intensityButton;
  private final JButton valButton;
  private final JButton greyButton;
  private final JButton sepiaButton;
  //brighten button and a text box that lets the user select how much to increase by
  private final JButton brightButton;
  private final JTextField brightVal;
  //darken button and a text box that lets the user select how much to decrease by
  private final JButton darkButton;
  private final JTextField darkVal;
  private final JButton blurButton;
  private final JButton sharpButton;

  /**
   * Constructs a view, setting up the arrangement of all the panels and labels.
   *
   * @param caption name of the window that is created
   */
  public ImageProcessorSwingView(String caption) {
    //names the window
    super(caption);

    //sets the size of the window
    setSize(800, 800);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //creates the main panel and makes it scrollable
    //main panel that we add to and its scrollable version
    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    //scroll bars around this main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //show an image with a scrollbar
    JPanel display = new JPanel();
    display.setLayout(new GridLayout(1, 0, 10, 10));
    display.setPreferredSize(new Dimension(100, 500));
    mainPanel.add(display);
    this.displayLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(this.displayLabel);
    imageScrollPane.setPreferredSize(new Dimension(100, 500));
    //adds it to the panel for images
    display.add(imageScrollPane);

    //adds the histogram
    this.histoLabel = new JLabel();
    JScrollPane histoScrollPane = new JScrollPane(this.histoLabel);
    histoScrollPane.setPreferredSize(new Dimension(100, 500));
    //adds the histogram to the panel for images
    display.add(histoScrollPane);

    //adds the messages label right below where the images are
    this.message = new JLabel("Messages");
    this.message.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(this.message);

    //file open
    JPanel loader = new JPanel();
    JPanel fileOpenPanel = new JPanel();
    fileOpenPanel.setLayout(new FlowLayout());
    loader.add(fileOpenPanel);
    this.loadButton = new JButton("Open a file");
    this.loadButton.setActionCommand("Load");
    fileOpenPanel.add(this.loadButton);
    mainPanel.add(loader);

    //file save
    JPanel saver = new JPanel();
    JPanel fileSavePanel = new JPanel();
    fileSavePanel.setLayout(new FlowLayout());
    saver.add(fileSavePanel);
    this.saveButton = new JButton("Save a file");
    this.saveButton.setActionCommand("Save");
    fileSavePanel.add(this.saveButton);
    mainPanel.add(saver);

    //Our commands that we use to modify an image
    JPanel commands = new JPanel();
    commands.setLayout(new GridLayout(5, 3));
    mainPanel.add(commands);

    //buttons for each of our commands
    this.redButton = new JButton("Red Component");
    this.greenButton = new JButton("Green Component");
    this.blueButton = new JButton("Blue Component");
    this.valButton = new JButton("Value");
    this.intensityButton = new JButton("Intensity");
    this.lumaButton = new JButton("Luma");
    this.greyButton = new JButton("Greyscale");
    this.horButton = new JButton("Horizontal Flip");
    this.verButton = new JButton("Vertical Flip");
    this.sepiaButton = new JButton("Sepia");
    this.blurButton = new JButton("Blur");
    this.sharpButton = new JButton("Sharpen");

    //set buttons for brightening and darkening separately, since they require additional input
    JPanel brightPanel = new JPanel();
    brightPanel.setLayout(new BoxLayout(brightPanel, BoxLayout.X_AXIS));
    this.brightButton = new JButton("Brighten");
    this.brightVal = new JTextField();
    brightPanel.add(this.brightButton);
    brightPanel.add(this.brightVal);

    JPanel darkPanel = new JPanel();
    darkPanel.setLayout(new BoxLayout(darkPanel, BoxLayout.X_AXIS));
    this.darkButton = new JButton("Darken");
    this.darkVal = new JTextField();
    darkPanel.add(this.darkButton);
    darkPanel.add(this.darkVal);

    //adds it to the panel
    commands.add(this.redButton);
    commands.add(this.blueButton);
    commands.add(this.greenButton);
    commands.add(this.valButton);
    commands.add(this.intensityButton);
    commands.add(this.lumaButton);
    commands.add(this.greyButton);
    commands.add(this.horButton);
    commands.add(this.verButton);
    commands.add(this.sepiaButton);
    commands.add(brightPanel);
    commands.add(darkPanel);
    commands.add(this.blurButton);
    commands.add(this.sharpButton);

    //makes the view visible
    setVisible(true);
  }

  /**
   * Renders out an image that will be displayed to the user along with its histogram.
   *
   * @param image the image that is to be visualized
   */
  @Override
  public void renderImage(Image image) {
    this.displayLabel.setIcon(new ImageIcon(image));
  }

  /**
   * Renders out a message about any process done or any errors in processing images.
   *
   * @param msg the message that we are to render out
   */
  @Override
  public void renderMessage(String msg) {
    this.message.setText(msg);
  }

  /**
   * Renders out a histogram that can visualize any number of components/analyses of an image.
   *
   * @param histogram an array that represents the values in the histogram
   * @throws IllegalArgumentException histogram provided does not have enough components
   */
  @Override
  public void renderHistogram(int[][] histogram) {
    if (histogram.length < 4) {
      throw new IllegalArgumentException("Histogram is not adequate for this view");
    }
    int maxVal = 0;
    //finds the maximum value for any component
    for (int c = 0; c < 4; c = c + 1) {
      for (int am = 0; am < histogram[0].length; am = am + 1) {
        if (maxVal < histogram[c][am]) {
          maxVal = histogram[c][am];
        }
      }
    }
    //creates the image that we will add to the histogram label
    BufferedImage hDisplay = new BufferedImage(histogram[0].length * 4, maxVal,
        BufferedImage.TYPE_INT_RGB);
    int[] colors = new int[]{Color.RED.getRGB(), Color.GREEN.getRGB(), Color.BLUE.getRGB(),
        Color.GRAY.getRGB()};
    for (int com = 0; com < 4; com = com + 1) {
      for (int a = 0; a < histogram[0].length; a = a + 1) {
        int value = 1;
        while (value < histogram[com][a]) {
          hDisplay.setRGB(a + (com * histogram[0].length), maxVal - value - 1, colors[com]);
          value = value + 1;
        }
      }
    }
    this.histoLabel.setIcon(new ImageIcon(hDisplay));
  }

  /**
   * Adds the features to a given Features object by linking buttons or keys to each one, depending
   * on the implementation.
   *
   * @param features what we assign actions or keys to
   */
  @Override
  public void addFeatures(Features features) {
    //adds the listener for the load button
    this.loadButton.addActionListener(evt -> {
      final JFileChooser fChooser = new JFileChooser(".");
      int retValue = fChooser.showOpenDialog(this);
      if (retValue == JFileChooser.APPROVE_OPTION) {
        features.loadFeature(fChooser.getSelectedFile().getAbsolutePath());
      }
    });
    //adds the listener for the save button
    this.saveButton.addActionListener(evt -> {
      final JFileChooser fChooser = new JFileChooser(".");
      int retValue = fChooser.showOpenDialog(this);
      if (retValue == JFileChooser.APPROVE_OPTION) {
        features.saveFeature(fChooser.getSelectedFile().getAbsolutePath());
      }
    });
    //adds the listeners for all the command buttons
    this.redButton.addActionListener(evt -> features.commandFeature("red"));
    this.greenButton.addActionListener(evt -> features.commandFeature("green"));
    this.blueButton.addActionListener(evt -> features.commandFeature("blue"));
    this.valButton.addActionListener(evt -> features.commandFeature("value"));
    this.intensityButton.addActionListener(evt -> features.commandFeature("intensity"));
    this.lumaButton.addActionListener(evt -> features.commandFeature("luma"));
    this.greyButton.addActionListener(evt -> features.commandFeature("greyscale"));
    this.horButton.addActionListener(evt -> features.commandFeature("horizontal"));
    this.verButton.addActionListener(evt -> features.commandFeature("vertical"));
    this.sepiaButton.addActionListener(evt -> features.commandFeature("sepia"));
    this.blurButton.addActionListener(evt -> features.commandFeature("blur"));
    this.sharpButton.addActionListener(evt -> features.commandFeature("sharpen"));
    //adds the listeners for the brightening and darkening commands
    this.brightButton.addActionListener(evt -> {
      try {
        int inc = Integer.parseInt(this.brightVal.getText());
        features.brightenFeature(inc);
      } catch (NumberFormatException e) {
        this.renderMessage("Please enter a valid number");
      }
    });
    this.darkButton.addActionListener(evt -> {
      try {
        int inc = Integer.parseInt(this.darkVal.getText());
        features.darkenFeature(inc);
      } catch (NumberFormatException e) {
        this.renderMessage("Please enter a valid number");
      }
    });
  }
}
