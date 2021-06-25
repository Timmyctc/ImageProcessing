package ie.gmit.dip;

import java.io.File;
import java.util.Scanner;

public class FileSearcher {
	
	//extension default is png 
	private final static String extension = ".png";
	private final static Scanner sc = new Scanner(System.in);

	//Public Method facilitating user input of File and Directory(Checks if they exist, delegates to FindFile method)
	public static String enterFile() throws Exception {
		
		
		
		//Prompts and other bits
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter File Name");
		System.out.println(ConsoleColour.RESET);
		String fileName = sc.nextLine().trim() + extension;	//Get the file name, append the extension. 
		System.out.println(ConsoleColour.BLUE);
		System.out.println("Enter Directory Path to Search");
		System.out.println(ConsoleColour.RESET);

		String delimiter = getOS();
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
		
		/*try {
			File target = new File(findFile(fileName, directory));	//Delegate to findFile method for retrieval if file exists
			return target.getAbsolutePath();
		} catch (Exception e) {
			
		}
		return null;*/
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
	

	//Delegated Method that conducts the real retrieval of the file (Probably too much validation but better safe than sorry)
	/*private static String findFile(String fileName, String directory) throws Exception {
		File[] fileList = new File(directory).listFiles();
		
		//Iterating through files in directory to find file with same name.
		for (int i = 0; i < fileList.length; i++) {
			File f = fileList[i];
			if (f.getName().equalsIgnoreCase(fileName)) {
				System.out.println(ConsoleColour.RED);
				System.out.println("Retrieving File...");
				System.out.println(ConsoleColour.RESET);
				return f.getAbsolutePath();
			}//End IF 		
		}//End For
		return null;	//This will never return null due to the validation in previous method
	}// End findFile*/
}// End FileSearcher



/*
--------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------
	
*/
