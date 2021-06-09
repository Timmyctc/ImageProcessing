package ie.gmit.dip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Menu {
	//Scanner obj to be used for various user inputs
	private final static Scanner sc = new Scanner(System.in);
	public static Kernel k = Kernel.BOX_BLUR.LAPLACIAN;
	
	
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
					BufferedImage image = ImageIO.read(new File(FileSearcher.enterFile()));
					//System.out.println(image); This writes out a lot of useful meta-data about the image.
					ImageProcess.readImage(image);
				} catch (Exception e) {
					//something
				}
				break;
			case 2:
				kernelSet.forEach(System.out::println);
				break;
			case 3:
				System.out.println("Enter the name of the filter you wish to use (Not case sensitive)");
				String s = getStringInput();
				try {
					setKernel(s);
				} catch (Exception e) {
					System.out.println("Not a Valid Filter, Try Again");
				}
				break;
			case 4:
				System.out.println("Coming Soon");
				//Does Something Eventually
				break;
			case 5:	
				if(k != null) {
				System.out.println("Current Kernel is " + k.toString());
				}
				else System.out.println("Select A Filter First");
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
		sc.close();
	}//End Display Menu

	 //Aesthetic Method to display menu options
	 private static void displayOptions() {
		 System.out.println();
		 System.out.println(ConsoleColour.BLACK);
			System.out.println("1) Enter Image Name"); // Ask user to specify the file to process. 										
			System.out.println("2) Display Available Filters"); // List the set of filters available in the class Kernel.java
			System.out.println("3) Select A Filter"); 		//Select a Filter by typing the name (Not case sensitive)									
			System.out.println("4) Does Nothing Yet"); // 
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
		int choice = -1;
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				//System.out.println("Not a valid input, Select a Valid Input");
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
}//End Class
