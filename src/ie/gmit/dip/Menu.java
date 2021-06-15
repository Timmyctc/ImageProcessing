package ie.gmit.dip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Menu {
	
	//Scanner obj to be used for various user input
	private final static Scanner sc = new Scanner(System.in);
	
	//Default value for Kernel
	public static Kernel k = Kernel.HORIZONTAL_LINES;
	
	
	//Set Of all Enums
	public static EnumSet<Kernel> kernelSet = EnumSet.allOf(Kernel.class);
	
	 static void displayMenu() throws Exception {
		 displayHeader();
		
		
		 //Menu Loop Boolean
		boolean loop = true;
		
		do {
			displayOptions();
			System.out.println(ConsoleColour.RESET);			
			int choice = getInput();
			
			switch (choice) {
			case 1:		//Enter Image to Apply Filter To
				try {
					BufferedImage inputImage = ImageIO.read(new File(FileSearcher.enterFile()));	//Attempt to get File information from user
					ImageProcess.readImage(inputImage, k);											//Process the file input by the user, pass image file and kernel to use 
				} catch (Exception e) {
					//Double Safety Net Validation here, Error will be caught in FileSearcher and kick us back to menu
				}
				break;
			case 2:
				System.out.println(ConsoleColour.BLUE);
				kernelSet.forEach(System.out::println);												//Enumset foreach method to display all enums in the kernel class
				System.out.println(ConsoleColour.RESET);
				break;
			case 3:
				
				//Loop boolean to keep prompting user for which kernel to use while they enter an incorrect value, (-1 exits the loop and returns to main menu)
				boolean setLoop = false;
				String s;

				do {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Enter the name of the filter you wish to use (Not case sensitive) Enter -1 to exit to Main Menu");
					System.out.println(ConsoleColour.RESET);
					s = getStringInput();
					try {
						if(s.equals("-1")) {														//User can enter -1 to return to main menu
							System.out.println("Going back to main Menu");
							break;
						}
						setKernel(s);																//try set kernel by passing s to the .valueOf() enum method
						setLoop = true;
					} catch (Exception e) {
						System.out.println(ConsoleColour.RED);
						System.out.println("Not a Valid Filter, Try Again");
						System.out.println(ConsoleColour.RESET);
						setLoop = false;
					} 
				} while (!setLoop);
				if (setLoop) {
					System.out.println(ConsoleColour.BLUE);
					System.out.println("Confirmed: Current Filter is Now " + s);					//when the user successfully changes kernel, output to confirm
					System.out.println(ConsoleColour.RESET);
					System.out.println();
				}
				break;
			case 4:
			//	createKernel();
				//Does Something Eventually
				break;
			case 5:	
				if(k != null) {																		//This is extra validation that is superflouous now we start with a defaul kernel
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
				System.out.println("Quitting");														//V straightforward, Break from loop to quit
				System.out.println(ConsoleColour.RESET);
			loop = false;
			break;
			default:
				System.out.println(ConsoleColour.RED);
				System.out.println("Not a valid input, Select a Valid Input");						//If users input a nonvalid value, prompt them for a valid value
				System.out.println(ConsoleColour.RESET);
			}
		} while (loop);
	}//End Display Menu

	//Aesthetic Method to display menu options
	 private static void displayOptions() {
		 System.out.println();
		 System.out.println(ConsoleColour.BLACK);
			System.out.println("1) Enter Image Name"); 			// Ask user to specify the file to process. 										
			System.out.println("2) Display Available Filters"); // List the set of filters available in the class Kernel.java
			System.out.println("3) Select A Filter"); 			//Select a Filter by typing the name (Not case sensitive)									
			System.out.println("4) Add A Filter"); 				//Dont think this is workable (Replace with display before/after)
			System.out.println("5) Display Current Filter");	//Display current kernel to user
			System.out.println("6) Quit");						//Terminate program
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

	//utility method to get user's menu choice input
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
	
	
	//Method to Set Kernel - Takes String argument and checks it against the Enum valueOf method.
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
