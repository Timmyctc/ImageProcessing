package ie.gmit.dip;

import java.io.File;
import java.util.Scanner;
import java.net.MalformedURLException;
import java.net.URL;

//Class for searching for File based on user input (local) or user provided hyperlink (web) 
public class FileSearcher {
	
	//extension default is png 
	private final static String extension = ".png";
	private final static Scanner sc = new Scanner(System.in);

	//Public Method facilitating user input of File and Directory(Checks if they exist, returns it if true)
	public static String enterFile() throws Exception {
		
		
		
		//Prompts and other bits
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter File Name");
		System.out.println(ConsoleColour.RESET);
		String fileName = sc.nextLine().trim() + extension;	//Get the file name, append the extension. 
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter Directory Path to Search");
		System.out.println(ConsoleColour.RESET);

		String delimiter = getOS();					//forward slash or backslash depending on OS (understand there is a method directly for this now but decided to leave as is) 
		String directory = sc.nextLine().trim();	//Get directory path
		
		String path = directory+delimiter+fileName;
		File target = new File(path);
		if(target.exists())
		{
			System.out.println(ConsoleColour.RED);
			System.out.println("Retrieving File...");
			System.out.println(ConsoleColour.RESET);
			return target.getAbsolutePath();
		}
		else {
			System.out.println(ConsoleColour.RED);
			System.out.println("No Such File Found!");	//If file or Directory is incorrect.
			System.out.println(ConsoleColour.RESET);
			return null;
		}
		
	}// End File

	public static String getOS() {
		String os = System.getProperty("os.name");
		String delimiter;
		if(os.contains("Windows"))
		{
			 delimiter = "\\";
		}
		else  {
			 delimiter = "//";
		}
		return delimiter;
	}

	//using sc maybe new scanner required
	public static URL enterFileURL() throws MalformedURLException {
		System.out.println(ConsoleColour.BLUE);
		//System.out.println("Enter File Name");
		//System.out.println(ConsoleColour.RESET);
		String urlFile = sc.nextLine().trim();
			URL url = new URL(urlFile);
		return url;
	}
}// End FileSearcher

