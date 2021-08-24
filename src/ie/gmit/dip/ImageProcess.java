package ie.gmit.dip;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageProcess {
private static Scanner sc = new Scanner(System.in);

	//In-use kernel (Can be set and reset by user)
		public static Kernel k;
		private static double bias = 0.0d;
		private static double multiFactor = 1;
	


	// method reads in image
	public static void readImage(BufferedImage inputImage, Kernel kern) throws IOException {
		k=kern;
		writeImage(inputImage);
	}// End Read Image Method

	
	//Method to Write an Image Out
		private static void writeImage(BufferedImage inputImage) throws IOException {
				//String outputFileName = getFileOutputName() + ".PNG";
				BufferedImage outputImage = convolute(inputImage);
				
				if (secondFilterCheck()) {
					Kernel additionalFilter = getAdditionalFilter();
					readImage(outputImage, additionalFilter);
				} else

			// Continue Writing the output image
			try {
				ImageIO.write(outputImage, "PNG", new File(getFileOutputName() + ".png"));	//outputFileName
			} catch (FileNotFoundException e) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Error Writing the File\n Outputting to default directory");	
				System.out.println(ConsoleColour.RESET);
				ImageIO.write(outputImage, "PNG", new File("output.png"));
			}
		}// End Write Image Method
	
	
		//check if user wants to add another filter
		private static boolean secondFilterCheck() {
			System.out.println(ConsoleColour.BLUE);
			System.out.println("Press 1 to add another filter, 2 to continue");
			System.out.println(ConsoleColour.RESET);
			int choice = Integer.parseInt(sc.next());
			if (choice == 1) {
				return true;
			} else
				return false;
		}


		//get the secondary/tertiary filters to add by user input (this doesnt change the actual kernel we have selected 
		//from main menu option, this is rather a temp kernel to apply on top of an existing filtered image)
	private static Kernel getAdditionalFilter() {
			Kernel additionalFilter = null;
			System.out.println(ConsoleColour.BLUE);
			Menu.kernelSet.forEach(System.out::println);
			System.out.println(ConsoleColour.RESET);

			boolean loop;
			do {
				System.out.println(ConsoleColour.BLUE);
				System.out.println("Enter the name of the filter you wish to use (Not case sensitive)");
				System.out.println(ConsoleColour.RESET);
				String newFilter = sc.next();
				try {
					additionalFilter = Kernel.valueOf(newFilter.toUpperCase());// try set kernel by passing input to temp filter
					loop = true;
				} catch (Exception e) {
					System.out.println(ConsoleColour.RED);
					System.out.println("Not a Valid Filter, Try Again");
					System.out.println(ConsoleColour.RESET);
					loop = false;
				}
			} while (!loop);
			if (loop) {
				System.out.println(ConsoleColour.BLUE);
				System.out.println("Confirmed: adding filter " + additionalFilter.toString()); // when the user
																								// successfully changes
				// kernel, output to confirm
				System.out.println(ConsoleColour.RESET);
				System.out.println();
			}
			return additionalFilter;
		}


	private static String getFileOutputName() throws FileNotFoundException {
		Scanner sc1 = new Scanner(System.in);
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter File Name for Output:");
		System.out.println(ConsoleColour.RESET);
			String outputPictureName = sc1.nextLine().trim();
			System.out.println(ConsoleColour.BLUE);
			System.out.println("Enter Output directory, leave blank for default output to project folder.");
			System.out.println(ConsoleColour.RESET);
			String outputDirectory = sc1.nextLine().trim();
			String delimiter = FileSearcher.getOS();
			
			// Check if the Output Directory Provided by the User exists, If it doesn't only
			// return the output File Name and we'll output to default project folder
			File f = new File(outputDirectory);
			if (!(f).exists()) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Error, Directory not found: Outputting to Default Project Folder");
				System.out.println(ConsoleColour.RESET);
				return outputPictureName;
			} else {
				return outputDirectory + delimiter + outputPictureName; 
																	
			}

		}//getFileOutputName

	//Method to convolute
	private static BufferedImage convolute(BufferedImage image) {	
		BufferedImage imageO = getImageChoice(image);
		BufferedImage output = new BufferedImage(imageO.getWidth(), imageO.getHeight(), imageO.getType());
		
		
		//Height and Width of Image
		int height = image.getHeight();
		int width = image.getWidth();
			
		
			// loops over each pixel (For Each Pixel)
			for (int xCoord = 0; xCoord < (width); xCoord++) {	//-2
				for (int yCoord = 0; yCoord < (height); yCoord++) {
					
					//Output Red, Green and Blue Values
					int outR, outG, outB, outA;
						
					//Temp red, green, blue values that will contain the total r/g/b values after kernel multiplication
					double red = 0, green = 0, blue = 0, alpha = 0;
					int outRGB = 0;
					
					
					// Loop over the Kernel (For each cell in kernel)
					//The offset is added to the xCoord and yCoord to follow the footprint of the kernel
					//The logic behind below is that all kernels are uneven numbers (so they can have a centre pixel, 3x3 5x5 etc),
					//the offset needs to run from negative half their length (rounded down) to the positive of half their length (rounded down)
					//This allows us to input kernels of various sizes (5x5, 9x9 etc) and the calculation should not be thrown off
						try {
							for ( int xOffset = Math.negateExact(k.getKernels().length/2); xOffset <= k.getKernels().length/2; xOffset++) {
								for (int yOffset = Math.negateExact(k.getKernels().length/2); yOffset <= k.getKernels().length/2; yOffset++) {
									
									
									//X coord and Y coord of pixel (add offset to match up the kernel with the pixels corresponding to the kernel's footprint)
									//I.E. 0,0 in a 3x3 kernel is the equivalent to the current pixel value + -1,-1 (i.e. 1 step back one step up) 
											//TODO	//very basic wrapping logic
										int realX = (xCoord - k.getKernels().length/ 2 +xOffset+  width) % width; 
										int realY = (yCoord - k.getKernels().length/ 2 +yOffset+  height) % height;
									

									int RGB = image.getRGB((realX), (realY));	//The RGB value for the pixel, will be split out below
									int A = (RGB >> 24) & 0xFF;	//Bitshift 24 to get alpha value 
									int R = (RGB >> 16) & 0xFF; //Bitshift 16 to get Red Value
									int G = (RGB >> 8) & 0xFF; //Bit Shift 8 to get Green Value
									int B = (RGB) & 0xFF;
																	
									//actual rgb * kernel logic 
									 red+=  (R*(k.getKernels()[yOffset + k.getKernels().length/2])[xOffset +k.getKernels().length/2] *multiFactor);
									 green +=  (G*k.getKernels()[yOffset+ k.getKernels().length/2][xOffset+k.getKernels().length/2] * multiFactor);		
									 blue +=  (B*k.getKernels()[yOffset +k.getKernels().length/2][xOffset+k.getKernels().length/2] * multiFactor);
									 alpha += (A*k.getKernels()[yOffset +k.getKernels().length/2][xOffset+k.getKernels().length/2] * multiFactor);
							
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Error");	
						}
						
						//Logic here prevents pixel going over 255 or under 0 
						//The "winner"of the max between the Colour value or 0(min pixel value) will be Math.min with 255 (the max value for a pixel)
					outR = (int) Math.min(Math.max((red+bias),0),255);
					outG = (int) Math.min(Math.max((green+bias),0),255);
					outB = (int) Math.min(Math.max((blue+bias),0),255);
					outA = (int) Math.min(Math.max((alpha+bias),0),255);
					
					//Reassembling the separate color channels into one variable again.
					outRGB =  outRGB |  (outA << 24);
					outRGB = outRGB | (outR << 16);
					outRGB = outRGB  | (outG << 8);
					outRGB = outRGB  | outB ;
					
					
					//Setting with the reassembled RGB value 
					//output.setRGB(xCoord, yCoord, outRGB);

					//Outputting with the individual color channels
					output.setRGB(xCoord, yCoord, new Color(outR,outG,outB).getRGB());	//this one works for sure
				}
			}
			System.out.println(ConsoleColour.RED);
			System.out.println("Converting...Please Wait.");
			System.out.println(ConsoleColour.RESET);
		return output;
	}//End Convolute mEthod

	
	//Allows user to choose output RGB or GS 
	private static BufferedImage getImageChoice(BufferedImage image) {
		
		//get int value corresponding to the various bufferedimage types 
		int currentType = image.getColorModel().getColorSpace().getType();
		boolean grayscale = (currentType==ColorSpace.TYPE_GRAY || currentType==ColorSpace.CS_GRAY);
		if(grayscale)	//If image is one of the two GS types output as GS 
		{
			System.out.println(ConsoleColour.RED);
			System.out.println("GrayScale Image detected, Outputting as GrayScale...");
			System.out.println(ConsoleColour.RESET);
			return image;
		}
		else {	//Else give user a choice to output as RGB or Convert to GS before applying kernel 
				System.out.println(ConsoleColour.BLUE);
				System.out.println("RGB Picture Input: Do you want to output to");
				System.out.println("1) Colour");
				System.out.println("2) Gray Scale\n");
				System.out.println(ConsoleColour.RESET);
				int imageTypeChoice = sc.nextInt();
				while(imageTypeChoice > 2 || imageTypeChoice <1 )
				{
					try {
						System.out.println(ConsoleColour.RED);
						System.out.println("Not a valid input\n1) Colour\n2)Gray Scale");
						System.out.println(ConsoleColour.RESET);
						imageTypeChoice = Integer.parseInt(sc.nextLine());
					} catch (Exception e) {
						
					}
				}
				switch(imageTypeChoice) {
				case 1:	//Return image as it is
					return image;
				case 2: //Delegate to utility method that actually converts the image to gs
					return(convertToGS(image));
				}
		}
		return image;	
	}
	
	//Method to convert an RGB image to GS
	private static BufferedImage convertToGS(BufferedImage image) {
		
		//width and height
		int width = image.getWidth();
		int height = image.getHeight();
		
		//looping over Pixels (For each pixel
		for(int y = 0; y < height; y++){
			  for(int x = 0; x < width; x++){ 
				  int pixel = image.getRGB((x), (y));	//current pixel
					int A = (pixel >> 24) & 0xFF;	//Bitshift 24 for Alpha
					int R = (pixel >> 16) & 0xFF; //Bitshift 16 to get Red Value
					int G = (pixel >> 8) & 0xFF; //Bit Shift 8 to get Green Value
					int B = (pixel) & 0xFF;		//Blue value
					
					//Average of the R, G and B values
					int average = (R+G+B)/3;
					
					//reassemble pixel
					pixel = (A<<24) | (average<<16) | (average<<8) | average;
					//set reassembled pixel value
					image.setRGB(x, y, pixel);
			  }
			}
		//return gs image
		return image;
	}//End method
	
	//Bias is a value added to the individual channels, rudimentary option to brighten or darken an image
	public static double getBias() {
		return bias;
	}


	public static void setBias(double bias) {
		ImageProcess.bias = bias;
	}

	//MultiFactor multiplies each cell of the kernel to increase / decrease the kernels effect. Also affects brightness so may need to be offset with bias
	public static double getMultiFactor() {
		return multiFactor;
	}


	public static void setMultiFactor(double multiFactor) {
		ImageProcess.multiFactor = multiFactor;
	}

}//End Class

/*	C:\Users\timmy\Desktop
*/