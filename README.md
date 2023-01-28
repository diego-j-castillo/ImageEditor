# Image Processor
This is the ImageProcessor for Assignment 6 of CS3500

## Project Structure
Program runs from the ImageProcessor.java file. It calls upon the modifyImages() method of the ImageProcessorController to start the processor in text mode. For a GUI mode, it constructs an
IPGUIController, which should open up a GUI for the user to interact with. 
For a simpler summary of the below classes and interfaces, open Assignment6Diagram.png in the res/ folder.

### Model
- Image - representation of an image as an object with some width and height in pixels, a number of components per pixel, and a maximum value for its pixel components
- AbstractImage - abstraction for Images that simplifies the creation of new Image implementaions
- ImageRGB - representation of an image with 3 components for R, G, and B (in that order)
- ImageProcessorModel - collection of Images that can each be stored and reffered to as a String
- ImageProcessorModelImpl - implementation of the ImageProcessorModel interface that uses a Map to store Images under a String key
- IPModelGUI - interface that extends the ImageProcessorModel interface and is used to implement methods that are liekly needed for GUI versions of the program
- IPModelGUIImpl - implementation of the IPModelGUI interface that delegates some method to an ImageProcessorModelImpl

### View
- ImageProcessorView - interface that requires that we can output an Image and a message to some defined output
- ImageProcessorTextView - implementation that displays the images and messages to the view as text to an Appendable output source
- ImageProcessorGUIView - interface that for view implementations that use a GUI to display and recieve user input
- ImageProcessorSwingView - implementation that uses the Swing library to create a GUI for the program

### Controller
- ImageProcessorController - controller interface that is meant to allow for the user to input commands that will modify images in a model
- ImageProcessorControllerImpl - implementation of the controller that uses a Readable object to detect inputs, executes commands on a stored Model, and outputs images and messages to a stored View
- Features - interface that lists out all of the unique features for a controller in a GUI version of the processor
- IPGUIController - implementation of the Features interface that stores the current image in a IPModelGUI as "current" and outputs its relevant details to a GUIView.
- Command - interface that represents modifications and commands on a model, allowing operations to be done on one or more images in the model
- ImageFileUtil -interface of utilities that allow the conversion to and from a file of a certain file type and another file type or Image object

#### Commands
- ACommand - abstraction of common functionality for commands to allow for easier implementation of new commands
- MixVis - visualizes an image in greyscale, using a combination of the three RGB values in a pixel. Currently supports value visualization
- HFlip - flips an image in the model horizontally and stores it in the given model
- VFlip - flips an image in the model vertically and stores it in the given model
- Brightness - changes the brightness of an image in the model and stores it (positive change is brighter, negative change is darker)
- Transform - uses a linear color transformation matrix to change the RGB values of an image. Currently supports iusalizing each RGB component, highlighting the intensity, higglighting the luma, and changing to sepia tone
- Transformation - enum that contains the right 3x3 matrix to produce a linear color transformation
- Filter - uses a kernel to apply a filter effect on an image. CUrrently supports blurring and sharpening
- Filters - enum that contains the kernel that we use to apply a filter on an image

#### ImageFileUtil
- ImagePPMUtil - allows for the conversion of images that support RGB components to and from an ASCII PPM file (P3, not P6)
- ImageGenUtil - allows for the conversion between 4 traditional image types (PNG, JPG, JPEG, BMP) to and from an RGB image

### Working Features 
Unless stated otherwise, the following features work for all entry modes.
- Visualizing individual RGB components of an image
- Visualizing the value of an image
- Visualizing the intensity of an image
- Visualizing the luma of an image
- Converting an image to greyscale
- Converting an image to sepia tone
- Flipping an image horizontally 
- Flipping an image vertically
- Blurring an image
- Sharpening an image
- Brightening an image by a set integer increment
- Darkening an image by a set integer increment
- Loading from an ASCII PPM file, PNG file, JPG file, JPEG file, and a BMP file
- Saving to an ASCII PPM file, PNG file, JPG file, JPEG file, and a BMP file
- Visualizing the histogram of the RGB components and intensity of an image (GUI only)
- Viewing the image as it would appear prior to saving (GUI only)

For exact instructions on how to directly enter the commands into the program in each of the entry modes, please see the USEME.md file.

## Running the Program (with sample scripts)
There are three distinct ways to run the program. You can read inputs from a file, read them from manual keyboard input, or use a GUI.

To designate which type of input you want, use `-file` to run from a file or `-text` to run with keyboard input. No arguments opens up a GUI. Any other input is considered invalid and outputs an error message to the system.

Below are samples for all input options. `sampleInput.txt` is already included in the res/ folder and can be used as a quick way to demonstrate all commands. 
For a screenshot of the program in action, see ProgramScreenshot.png in the res/ folder. 
For a sample image to run in GUI mode, see sampleImage.png in the res/folder.

In the command line arguments for running ImageProcessor.java:
- Run from a file in text mode
`-file res/sampleInput.txt`  
- Run using keyboard input in text mode
`-text`

`res/sampleInput.txt` will not work when trying to run a script file with the JAR file (must use `sampleInputCommandLine.txt` in its place). Please see the USEME.md file for more details on how to run the Assignment5.jar file in the res/ folder. 

## Design Changes from Assignment 5
- now uses `-file` and `-text` as commands line arguments, instead of `-script-file` and `-manual` to better match with assignment requirements
- removed `-script-args` as a way to run the program from the command line arguments, since we were told to only allow for a specific three input methods

## Design Changes from Assignment 4
- Removed CompVis command since all of its functionality could be done through the Transform command
- Removed option to get intensity and luma from MixVis command since it was better suited for the Transform command
- Added filterImage() to the ImageProcessorModel interface so that the process of filtering an image was done by the model itself, with the commands only specifying how a new image was stored and what kernel to use
- Added transformImage() to the ImageProcessorModel interface so that the process of transforming an image was done by the model itself, with the commands only specifying how a new image was stored and what transformation matrix to use

## Image Rights
Images used in this project were created by us. We authorize their use in this project.