package ie.gmit.dip;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcess {

	// method reads in image
	public static void readImage(BufferedImage inputImage) throws IOException {
		writeImage(inputImage);
	}// End Read Image Method

	//Method to Write an Image Out
		private static void writeImage(BufferedImage inputImage) {
			double[][] kernel = new double[][] //Kernel is hardcoded needs to be settable
				{ 
				{0, -1, 0},
				{-1, 4, -1},
				{0, -1, 0}
				};
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
		
		//Using RGBS
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		//USING Kernel
		//BufferedImage output2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		//Height and Width of Image
		int height = image.getHeight();
		int width = image.getWidth();
			
		try {
			// loops over each pixel (For Each Pixel)
			for (int xc = 1; xc < width-2; xc++) {	//-2
				for (int yc = 1; yc < height-2; yc++) {
					
					//Output Red, Green and Blue Values
					int outR, outG, outB;
						
					//Temp red, green, blue values that will contain the total r/g/b values after kernel multiplication
					int red = 0, green = 0, blue = 0, alpha = 0, outRGB = 0;
					
					// Loop over the Kernel (For each cell in kernel) -----------------------------------------------------------------------------
					for (int xOffset = -1; xOffset <= 1; xOffset++) {
						for (int yOffset = -1; yOffset <= 1; yOffset++) {
							
							int realX = (xc + width + xOffset) % width;
							int realY = (yc + height + yOffset) % height;
							
							//int RGB = (int) imageMatrix[xc][yc];
							int RGB = image.getRGB((realX), (realY));
							//int A = (RGB >> 24) & 0xFF;
							int R = (RGB >> 16) & 0xFF; //Bitshift 16 to get Red Value
							int G = (RGB >> 8) & 0xFF; //Bit Shift 8 to get Green Value
							int B = (RGB) & 0xFF;
							
							 red +=  (int) (R*kernel[xOffset +1][yOffset+1]);
							 green +=  (int) (G*kernel[xOffset +1][yOffset+1]);
							 blue +=(int) (B*kernel[xOffset +1][yOffset+1]);
							// alpha += (int) (A*kernel[xOffset +1][yOffset+1]);
						}
					}
					outR = Math.min(Math.max((red),0),255);
					outG = Math.min(Math.max((green),0),255);
					outB = Math.min(Math.max((blue),0),255);
					//outA = Math.min(Math.max((alpha),0),255);
					outRGB =  outRGB | (alpha << 24);
					outRGB = outRGB | (red << 16);
					outRGB = outRGB  | (green << 8);
					outRGB = outRGB  | blue;
					
					output.setRGB(xc, yc, new Color(outR,outG,outB).getRGB());
					//output2.setRGB(xc, yc, Math.min(Math.max((kernelSum),0),255));
				}
			}
		} catch (Exception e) {
			System.out.println("Failed");
		}
		return output;
	}//End Convolute mEthod
}//End ImageProcess Method

/*	C:\Users\timmy\Desktop */