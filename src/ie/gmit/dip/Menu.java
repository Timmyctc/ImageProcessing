package ie.gmit.dip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Menu {
	
	//private static int customCount = 1;
	//Scanner obj to be used for various user input
	private final static Scanner sc = new Scanner(System.in);
	
	//Default value for Kernel
	public static Kernel k = Kernel.HORIZONTAL_LINES;
	
	
	//Set Of all Enums
	public static EnumSet<Kernel> kernelSet = EnumSet.allOf(Kernel.class);
	
	 static void displayMenu() throws Exception {
		 displayHeader();

		//Loop Bool
		boolean loop = true;
		do {
			displayOptions();
			System.out.println(ConsoleColour.RESET);			
			int choice = getInput();
			switch (choice) {
			case 1:	
				try {
					BufferedImage inputImage = ImageIO.read(new File(FileSearcher.enterFile()));
					ImageProcess.readImage(inputImage, k);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println(ConsoleColour.BLUE);
				kernelSet.forEach(System.out::println);
				System.out.println(ConsoleColour.RESET);
				break;
			case 3:
				boolean set = false;
				
				String s;

				do {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Enter the name of the filter you wish to use (Not case sensitive)");
					System.out.println(ConsoleColour.RESET);
					s = getStringInput();
					try {
						setKernel(s);
						set = true;
					} catch (Exception e) {
						System.out.println(ConsoleColour.RED);
						System.out.println("Not a Valid Filter, Try Again");
						System.out.println(ConsoleColour.RESET);
						set = false;
					} 
				} while (!set);
				if (set) {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Confirmed: Current Filter is Now " + s);
					System.out.println(ConsoleColour.RESET);
					System.out.println();
				}
				break;
			case 4:
			//	createKernel();
				//Does Something Eventually
				break;
			case 5:	
				if(k != null) {
					System.out.println(ConsoleColour.BLUE);
				System.out.println("Current Kernel is " + k.toString());
				System.out.println(ConsoleColour.RESET);
				}
				else if(k == null) {
					System.out.println(ConsoleColour.RED); 
					System.out.println("Select A Filter First");
					System.out.println(ConsoleColour.RESET);
				}
				break;
			case 6:
				System.out.println(ConsoleColour.RED);
				System.out.println("Quitting");
				System.out.println(ConsoleColour.RESET);
			loop = false;
			break;
			default:
				System.out.println(ConsoleColour.RED);
				System.out.println("Not a valid input, Select a Valid Input");
				System.out.println(ConsoleColour.RESET);
			}
		} while (loop);
	}//End Display Menu

	//Aesthetic Method to display menu options
	 private static void displayOptions() {
		 System.out.println();
		 System.out.println(ConsoleColour.BLACK);
			System.out.println("1) Enter Image Name"); // Ask user to specify the file to process. 										
			System.out.println("2) Display Available Filters"); // List the set of filters available in the class Kernel.java
			System.out.println("3) Select A Filter"); 		//Select a Filter by typing the name (Not case sensitive)									
			System.out.println("4) Add A Filter"); // 
			System.out.println("5) Display Current Filter");
			System.out.println("6) Quit");	//Terminate program
			System.out.println("\nSelect Option [1-6]>");
			System.out.println(ConsoleColour.RESET);
	}//end displayOptions

	//Aesthetic Method to display Menu Header
	private static void displayHeader() {
		System.out.println(ConsoleColour.WHITE_BOLD);
		System.out.println(ConsoleColour.BLUE_BACKGROUND);
		System.out.println("***************************************************");
		System.out.println("* GMIT - Dept. Computer Science & Applied Physics *");
		System.out.println("*                                                 *");
		System.out.println("*           Image Filtering System V0.1           *");
		System.out.println("*     H.Dip in Science (Software Development)     *");
		System.out.println("*                                                 *");
		System.out.println("***************************************************");
		System.out.print(ConsoleColour.RESET);	
	}//End Header

	//utility method to get users menu choice input
	private static int getInput() {
		Scanner sc2 = new Scanner(System.in);
		int choice = -1;
			try {
				choice = Integer.parseInt(sc2.nextLine());
			} catch (Exception e) {
				// System.out.println("Not a valid input, Select a Valid Input");
		}
		return choice;
	}//End GetInput Method

	//Utility Method to get String Input from User (Choosing Kernel)
	private static String getStringInput() {
		String s = sc.nextLine().toUpperCase();
		return s;
		}	
	
	//utility method to Set current Kernel
	private static void setKernel(String s) {
		k = Kernel.valueOf(s);
	}
	
	 /*private static void createKernel() {
		
		 System.out.println(ConsoleColour.BLUE);
		 System.out.println("What Size Filter:\n1) 3x3\n2) 5x5\n3) 9x9");
		 System.out.println(ConsoleColour.RESET);
		 int kernelSizeChoice = -1;
		 int kernelSize = 0;
			while (kernelSizeChoice <1 || kernelSizeChoice >3)
			{
				System.out.println(ConsoleColour.RED);
				System.out.println("Not a valid Input, Select a valid Input");
				System.out.println(ConsoleColour.RESET);
				kernelSizeChoice = getInput();
			}
			switch(kernelSizeChoice) {
			case 1: 
				kernelSize = 3;
				break;
			case 2:
				kernelSize = 5;
				break;
			case 3:
				kernelSize = 9;
				break;
				
				default: System.out.println("Not Good");				
			}
			System.out.println("Enter Values");
			try (Scanner kScan = new Scanner(System.in)) {
				double[][] newK = new double[kernelSize][kernelSize];
				for(int i = 0; i<newK.length; i++) {
					for(int j = 0; j<newK[i].length; j++) {
						newK[i][j] = kScan.nextDouble();
					}
				}
			}
		}*/

	
}//End Class
