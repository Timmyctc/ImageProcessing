package ie.gmit.dip;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageProcess {
private final static Scanner sc = new Scanner(System.in);

	//In-use kernel (Can be set and reset by user)
		public static Kernel k = Kernel.BOX_BLUR.LAPLACIAN;
		
	// method reads in image
	public static void readImage(BufferedImage inputImage) throws IOException {
		writeImage(inputImage);
	}// End Read Image Method

	
	//does nothing yet
	void setKernel(String kernelChoice){
		k = Kernel.valueOf(kernelChoice);
	}//
	
	//Method to Write an Image Out
		private static void writeImage(BufferedImage inputImage) {
			double[][] kernel = new double[][] //Kernel is hardcoded needs to be settable
				{
				{-0.00391, -0.01563, -0.02344, -0.01563, -0.00391},
				{-0.01563, -0.06250, -0.09375, -0.06250, -0.01563},
				{-0.02344, -0.09375, 1.85980, -0.09375, -0.02344},
				{-0.01563, -0.06250, -0.09375, -0.06250, -0.01563},
				{-0.00391, -0.01563, -0.02344, -0.01563, -0.00391}
				};
				System.out.println(Math.negateExact(kernel.length/2));
				System.out.println(kernel.length/2);
				BufferedImage outputImage = convolute(kernel,inputImage);	
				
			// Writing the output image
			try {
				ImageIO.write(outputImage, "PNG", new File("output.png"));
			} catch (IOException e) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Error Writing the File");	//Realistically this shouldnt throw
				System.out.println(ConsoleColour.RESET);
			}
		}// End Write Image Method
	
	
	//method to convolve
	private static BufferedImage convolute(double[][]kernel, BufferedImage image) {
		int imageType = getImageChoice(image);
		//Using RGBS
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
		
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
							for ( int xOffset = Math.negateExact(kernel.length/2); xOffset <= kernel.length/2; xOffset++) {
								for (int yOffset = Math.negateExact(kernel.length/2); yOffset <= kernel.length/2; yOffset++) {
									
									int realX = (xCoord +  xOffset );
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
									 
									 red +=  (R*kernel[yOffset + kernel.length/2][xOffset +kernel.length/2]);
									 green +=  (G*kernel[yOffset+ kernel.length/2][xOffset+kernel.length/2]);		//reverse
									 blue +=  (B*kernel[yOffset +kernel.length/2][xOffset+kernel.length/2]);
									 alpha += (A*kernel[yOffset +kernel.length/2][xOffset+kernel.length/2]);
								}
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}
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

	private static int getImageChoice(BufferedImage image) {
		int outputImageType = 1;
		int currentType = image.getColorModel().getColorSpace().getType();
		boolean grayscale = (currentType==ColorSpace.TYPE_GRAY || currentType==ColorSpace.CS_GRAY);
		if(grayscale)
		{
			System.out.println(ConsoleColour.RED);
			System.out.println("GrayScale Image detected, Outputting as GrayScale...");
			System.out.println(ConsoleColour.RESET);
			return 10;
		}
		else { 
				System.out.println("RGB Picture Input: Do you want to output to");
				System.out.println("1) Colour");
				System.out.println("2) Gray Scale");
				int imageTypeChoice = sc.nextInt();
				while(imageTypeChoice > 2 || imageTypeChoice <1 )
				{
					System.out.println("Not a valid input\n1) Colour\n2)Gray Scale");
					imageTypeChoice = sc.nextInt();
				}
				switch(imageTypeChoice) {
				case 1:
					outputImageType = 1;
					break;
						
				case 2: 
					outputImageType = 10;
				}
				
			return outputImageType;
		}	
	}
}//End Class

/*	C:\Users\timmy\Desktop 
{
				{-0.00391, -0.01563, -0.02344, -0.01563, -0.00391},
				{-0.01563, -0.06250, -0.09375, -0.06250, -0.01563},
				{-0.02344, -0.09375, 1.85980, -0.09375, -0.02344},
				{-0.01563, -0.06250, -0.09375, -0.06250, -0.01563},
				{-0.00391, -0.01563, -0.02344, -0.01563, -0.00391}
				};

*/