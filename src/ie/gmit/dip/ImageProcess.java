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
private final static Scanner sc = new Scanner(System.in);

	//In-use kernel (Can be set and reset by user)
		public static Kernel k;
		
	// method reads in image
	public static void readImage(BufferedImage inputImage, Kernel kern) throws IOException {
		k=kern;
		writeImage(inputImage);
	}// End Read Image Method

	
	//Method to Write an Image Out
		private static void writeImage(BufferedImage inputImage) throws IOException {
				String outputFileName = getFileOutputName() + ".PNG";
				BufferedImage outputImage = convolute(inputImage);
			// Writing the output image
			try {
				ImageIO.write(outputImage, "PNG", new File(outputFileName));
			} catch (FileNotFoundException e) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Error Writing the File\n Outputting to default directory");	
				System.out.println(ConsoleColour.RESET);
				ImageIO.write(outputImage, "PNG", new File("output.png"));
			}
		}// End Write Image Method
	
	
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
			
			//Check if the Output Directory Provided by the User exists, If it doesn't  only return the output File Name and we'll output to default project folder
			File f = new File(outputDirectory);
			if(!(f).exists()) return outputPictureName;
			 return outputDirectory + "\\" + outputPictureName;					//this line may cause incompatibility in linux systems due to backslashess
			}	

	//method to convolve
	private static BufferedImage convolute(BufferedImage image) {
		
		
		BufferedImage imageO = getImageChoice(image);
		
		//
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
					//This allows us to input kernels of various lengths and the calculation should not be thrown off
						try {
							for ( int xOffset = Math.negateExact(k.getKernels().length/2); xOffset <= k.getKernels().length/2; xOffset++) {
								for (int yOffset = Math.negateExact(k.getKernels().length/2); yOffset <= k.getKernels().length/2; yOffset++) {
									
									
									//X coord and Y coord of pixel (add offset to match up the kernel with the pixels corresponding to the kernel's footprint)
									//I.E. 0,0 in a 3x3 kernel is the equivalent to the current pixel value + -1,-1 (i.e. 1 step back one step up) 
										int realX = (xCoord +  xOffset);
										if(realX < 0) realX = (width + realX) % width;	//TODO
										int realY = (yCoord +  yOffset);
										if(realY < 0) realY = (height + realY) % height;	//TODO	//very basic wrapping logic
								
									
									
									//Experimental X and Y coords (Trying to get wrapping/ sampling working. Unsure how yet TODO
									int testX = (xCoord - k.getKernels().length/2 + xOffset + width) % width;
									int testY = (yCoord - k.getKernels().length/2 + yOffset + height) % height;

									int RGB = image.getRGB((realX), (realY));	//The RGB value for the pixel, will be split out below
									int A = (RGB >> 24) & 0xFF;	//Bitshift 24 to get alpha value 
									int R = (RGB >> 16) & 0xFF; //Bitshift 16 to get Red Value
									int G = (RGB >> 8) & 0xFF; //Bit Shift 8 to get Green Value
									int B = (RGB) & 0xFF;
									
									 //red +=  (R*kernel[yOffset +1][xOffset+1]);
									 //green +=  (G*kernel[yOffset +1][xOffset+1]);		//reverse
									 //blue +=  (B*kernel[yOffset +1][xOffset+1]);
									 //alpha += (A*kernel[yOffset +1][xOffset+1]);
									 
									 red+=  (R*k.getKernels()[yOffset + k.getKernels().length/2][xOffset +k.getKernels().length/2]);
									 green +=  (G*k.getKernels()[yOffset+ k.getKernels().length/2][xOffset+k.getKernels().length/2]);		//reverse
									 blue +=  (B*k.getKernels()[yOffset +k.getKernels().length/2][xOffset+k.getKernels().length/2]);
									 alpha += (A*k.getKernels()[yOffset +k.getKernels().length/2][xOffset+k.getKernels().length/2]);
									
							
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;	//TODO
						}
						
						//Logic here prevents pixel going over 255 or under 0 
						//The "winner"of the max between the Colour value or 0(min pixel value) will be Math.min with 255 (the max value for a pixel)
					outR = (int) Math.min(Math.max((red),0),255);
					outG = (int) Math.min(Math.max((green),0),255);
					outB = (int) Math.min(Math.max((blue),0),255);
					outA = (int) Math.min(Math.max((alpha),0),255);
					
					//Reassembling the separate color channels into one variable again.
					outRGB =  outRGB |  (outA << 24);
					outRGB = outRGB | (outR << 16);
					outRGB = outRGB  | (outG << 8);
					outRGB = outRGB  | outB ;
					
					
					//Setting with the reassembled RGB value 
					//output.setRGB(xCoord, yCoord, outRGB);

					//Outputting with the individual color channels
					output.setRGB(xCoord, yCoord, new Color(outR,outG,outB).getRGB());	//TODO this one works for sure
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
	
}//End Class

/*	C:\Users\timmy\Desktop 
{
				{-2, -2, -2, -2, -2},
				{-2, -1, -1, -1, -2},
				{0, 0.5, 1, 0.5, -0},
				{2, 1, 1, 1, 2},
				{2, 2, 2, 2, 2}
				};

*/