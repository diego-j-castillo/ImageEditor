## Running the JAR from the Command Line (with sample scripts)
There are three distinct ways to run the program. You can read inputs from a file, read them from manual keyboard input, or use a GUI.

To designate which type of input you want, use `-file` to run from a file or `-text` to run with keyboard input. No arguments opens up a GUI. Any other input is considered invalid and outputs an error message to the system.

Below are samples for all input options. `sampleInputCommandLine.txt` is already included in the res/ folder and can be used as a quick way to demonstrate all commands. 
For a screenshot of the program in action, see ProgramScreenshot.png in the res/ folder. 
For a sample image to run in GUI mode, see sampleImage.png in the res/folder.

In the command line arguments for running Assignment5.jar in the res/ folder:
- Run from a file 
`-file sampleInputCommandLine.txt` 
- Run using keyboard input in text mode
`-text` 

The script file listed above works only for the JAR file as it assumes that we are already in the res/ folder for its relative directories. Please see the README.md file for more details on how to run the ImageProcessor.java file.

## Guide for GUI Mode
- Load an image by pressing "Open a file" and selecting a valid image to load
- Save the image being worked on by pressing "Save a file" and choosing a name and directory
- "Red Component" to visualize the red component of the image
- "Green Component" to visualize the green component of the image
- "Value" to visualize the value of the image
- "Intensity" to visualize the intensity of the image
- "Luma" to visualize the luma of the image
- "Greyscale" to convert the image to greyscale
- "Horizontal Flip" to flip an image horizontally
- "Vertical Flip" to flip an image vertically 
- "Sepia" to convert the image to sepia tone
- "Brighten" with an integer increment in the text field to increase the brightness by that amount
- "Darken" with an integer increment in the text field to decrease the brightness by that amount
- "Blur" to blur the image
- "Sharpen" to sharpen the image

## Command List for Text Mode
Commands are structured as a series of inputs separated by a space or newline. The first argument in each command is necessary, while the others are to be specified by the user. The order of input matters, so when you call a command, it will only ever read the inputs to the right in the order of how they are given.
- Highlight the red components of an image
`red-component source-image image-dest`
- Highlight the green components of an image
`green-component source-image image-dest`
- Highlight the blue components of an image
`blue-component source-image image-dest`
- Highlight the value component of an image
`value-component source-image image-dest`
- Highlight the intensity components of an image
`intensity-component source-image image-dest`
- Highlight the luma components of an image
`luma-component source-image image-dest`
- Convert an image to greyscale
`greyscale source-image image-dest`
- Convert an image to sepia tone
`sepia source-image image-dest`
- Flip the contents of an image horizontally
`horizontal-flip source-image image-dest`
- Flip the contents of an image vertically
`vertical-flip source-image image-dest`
- Brighten an image (by an integer increment 0 or greater)
`brighten increment source-image image-dest`
- Darken an image (by an integer increment 0 or greater)
`darken increment source-image image-dest`
- Add a blur effect to an image
`blur source-image image-dest`
- Add a sharpening effect to an image
`sharpen source-image image-dest`
- Load an image from a file (currently supports P3 PPM, PNG, JPG, JPEG, and BMP)
`load file-path image-dest`
- Save an image to a file (currently supports P3 PPM, PNG, JPG, JPEG, and BMP)
`save file-path source-image`
- Force the program to quit (useful for manual input)
`quit`

## Command Examples
The following sequence of commands represents a sample of possible commands as they would be entered at runtime. This series of sample inputs will run through all commands as they should be entered when running Assignment5.jar from the res/ folder.

`load myPhoto.ppm myPhoto`

`red-component myPhoto myRed`

`green-component myPhoto myGreen`

`blue-component myPhoto myBlue`

`value-component myPhoto myValue`

`intensity-component myPhoto myIntensity`

`luma-component myPhoto myLuma`

`horizontal-flip myPhoto myHFlip`

`vertical-flip myPhoto myVFlip`

`brighten 50 myPhoto myBright`

`darken 50 myPhoto myDark`

`blur myPhoto myBlur`

`sharpen myPhoto mySharp`

`sepia myPhoto mySepia`

`greyscale myPhoto myGrey`

`save copyPhoto.ppm myPhoto`

`save copyPhoto.jpeg myPhoto`

`save copyPhoto.jpg myPhoto`

`save copyPhoto.png myPhoto`

`save copyPhoto.bmp myPhoto`

`load copyPhoto.jpg jP`

`load copyPhoto.jpeg jeP`

`load copyPhoto.bmp bP`

`load copyPhoto.png pP`

`quit`
