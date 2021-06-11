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
		public static Kernel k = Kernel.HORIZONTAL_LINES;
		
	// method reads in image
	public static void readImage(BufferedImage inputImage, Kernel kern) throws IOException {
		k=kern;
		writeImage(inputImage);
	}// End Read Image Method

	
	//Method to Write an Image Out
		private static void writeImage(BufferedImage inputImage) throws IOException {
				//System.out.println("Debug:" + Math.negateExact(k.getKernels().length/2));
				//System.out.println("Debug:" + k.getKernels().length/2);
				String outputFileName = getFileOutputName() + ".PNG";
				BufferedImage outputImage = convolute(inputImage);	
				
				
				
				
			// Writing the output image
			try {
				ImageIO.write(outputImage, "PNG", new File(outputFileName));
			} catch (FileNotFoundException e) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Error Writing the File\n Outputting to default directory");	//Realistically this shouldnt throw
				System.out.println(ConsoleColour.RESET);
				ImageIO.write(outputImage, "PNG", new File("output.png"));
			}
		}// End Write Image Method
	
	
	private static String getFileOutputName() {
		//Scanner sc = new Scanner(System.in);
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter File Name for Output:");
		System.out.println(ConsoleColour.RESET);
			String s = sc.nextLine().trim();
			System.out.println(ConsoleColour.BLUE);
			System.out.println("Enter Output directory, leave blank for default output to project folder.");
			System.out.println(ConsoleColour.RESET);
			String s1 = sc.nextLine().trim();
			//sc.close();
			if(s1.length()<3) return s;
			else return s1 + "\\" + s;
			}	


	//method to convolve
	private static BufferedImage convolute(BufferedImage image) {
		
		//TODO rework to properly flag a RGB to GS output (Needs to be properly converted)
		BufferedImage imageO = getImageChoice(image);
		
		//Run Method to Convert to GS here 
		
		//
		BufferedImage output = new BufferedImage(imageO.getWidth(), imageO.getHeight(), imageO.getType());
		
		//USING Kernel
		//BufferedImage output2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		//Height and Width of Image
		int height = image.getHeight();
		int width = image.getWidth();
			
		
			// loops over each pixel (For Each Pixel)
			for (int xCoord = 1; xCoord < (width); xCoord++) {	//-2
				for (int yCoord = 0; yCoord < (height); yCoord++) {
					
					//Output Red, Green and Blue Values
					int outR, outG, outB, outA;
						
					//Temp red, green, blue values that will contain the total r/g/b values after kernel multiplication
					double red = 0, green = 0, blue = 0, alpha = 0, outRGB = 0;
					
					// Loop over the Kernel (For each cell in kernel)
					//The offset is added to the xCoord and yCoord to follow the footprint of the kernel 
					 
						try {
							for ( int xOffset = Math.negateExact(k.getKernels().length/2); xOffset <= k.getKernels().length/2; xOffset++) {
								for (int yOffset = Math.negateExact(k.getKernels().length/2); yOffset <= k.getKernels().length/2; yOffset++) {
									
									int realX = (xCoord +  xOffset);
									int realY = (yCoord +  yOffset);
									

									int RGB = image.getRGB((realX), (realY));	//xCoord + xOffset, yCoord + yOffset
									int A = (RGB >> 24) & 0xFF;
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
									
									//red +=  (R*kernel[yOffset + kernel.length/2][xOffset +kernel.length/2]);
									//green +=  (G*kernel[yOffset+ kernel.length/2][xOffset+kernel.length/2]);		//reverse
									//blue +=  (B*kernel[yOffset +kernel.length/2][xOffset+kernel.length/2]);
									//alpha += (A*kernel[yOffset +kernel.length/2][xOffset+kernel.length/2]);
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}
						
						//Prevents pixel going over 255 or under 0 
					outR = (int) Math.min(Math.max((red),0),255);
					outG = (int) Math.min(Math.max((green),0),255);
					outB = (int) Math.min(Math.max((blue),0),255);
					outA = (int) Math.min(Math.max((alpha),0),255);
					//outRGB =  outRGB | (alpha << 24);
					//outRGB = outRGB | (red << 16);
					//outRGB = outRGB  | (green << 8);
					//outRGB = outRGB  | blue;
					
					output.setRGB(xCoord, yCoord, new Color(outR,outG,outB).getRGB());
					//output2.setRGB(xc, yc, Math.min(Math.max((kernelSum),0),255));
				}
			}
		return output;
	}//End Convolute mEthod

	
	//Allows user to choose output RGB or GS 
	private static BufferedImage getImageChoice(BufferedImage image) {
		//Scanner sc = new Scanner(System.in);
		int currentType = image.getColorModel().getColorSpace().getType();
		boolean grayscale = (currentType==ColorSpace.TYPE_GRAY || currentType==ColorSpace.CS_GRAY);
		if(grayscale)
		{
			System.out.println(ConsoleColour.RED);
			System.out.println("GrayScale Image detected, Outputting as GrayScale...");
			System.out.println(ConsoleColour.RESET);
			return image;
		}
		else { 
				System.out.println(ConsoleColour.BLUE);
				System.out.println("RGB Picture Input: Do you want to output to");
				System.out.println("1) Colour");
				System.out.println("2) Gray Scale\n");
				System.out.println(ConsoleColour.RESET);
				int imageTypeChoice = sc.nextInt();
				while(imageTypeChoice > 2 || imageTypeChoice <1 )
				{
					System.out.println(ConsoleColour.RED);
					System.out.println("Not a valid input\n1) Colour\n2)Gray Scale");
					System.out.println(ConsoleColour.RESET);
					imageTypeChoice = sc.nextInt();
				}
				switch(imageTypeChoice) {
				case 1:
					return image;
				case 2: 
					return(convertToGS(image));
				}
				//sc.close();
			
		}
		return image;	
	}
	
	private static BufferedImage convertToGS(BufferedImage image) {
		
		//width and height
		int width = image.getWidth();
		int height = image.getHeight();
		
		//looping over Pixels (For each pixel
		for(int y = 0; y < height; y++){
			  for(int x = 0; x < width; x++){
				  
				  int pixel = image.getRGB((x), (y));	//xCoord + xOffset, yCoord + yOffset
					int A = (pixel >> 24) & 0xFF;
					int R = (pixel >> 16) & 0xFF; //Bitshift 16 to get Red Value
					int G = (pixel >> 8) & 0xFF; //Bit Shift 8 to get Green Value
					int B = (pixel) & 0xFF;
					
					//Average of the R, G and B values
					int average = (R+G+B)/3;
					
					//reassemble pixel
					pixel = (A<<24) | (average<<16) | (average<<8) | average;
					image.setRGB(x, y, pixel);
			  }
			}

		return image;
	}
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