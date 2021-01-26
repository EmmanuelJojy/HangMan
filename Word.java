    import java.io.*;
    import java.util.*;
    
    /** 
     * {@code Word} class reads words from file "{@code fin.txt}".<br> 
     * In any case if the file is unavailable the program will be forced to terminate via the {@code System.exit()} method.
     * 
     * <p>All fields and methods except {@code getWord()} have private access specifier.</p>
     * 
     * <p>The application may abruptly terminate because of a missing file or stream error. 
     * Please provide the file "{@code fin.txt}" terminated by "{@code ##}" as the word pool.</p>
     * 
     * <p>Copyright (c) 2021. All rights reserved to Emmanuel Jojy. Use is subject to the above conditions.</p>
     * 
     * @author Emmanuel Jojy
     * 
     */
    
class Word{
    
	/**
	 * Bank for all words from the specified file. Intended to be accessed only by the {@code Word} class.
	 */
        private static ArrayList<String> words = new ArrayList<String>();
        
        /**
         * A boolean flag to indicate the words have already been read. 
         * This was an improvement after realizing multiple read from the smae UNCHANGED file could cause ineffeciency.
         */
	private static boolean readOnce = false;
	
	/**
	 * Default constructor left as is, since there are no instance fields specific to the class. All fields are of type static.
	 */
	public Word(){
	   }
	
	/**
	 * Reads from {@code "fin.txt"} and adds it to {@code words}.
	 */
	private static void read(){
		try{
			// FileInputStream + BufferedReader
			FileInputStream fin = new FileInputStream("fin.txt");
			BufferedReader obj = new BufferedReader(new InputStreamReader(fin));
		
			String s = obj.readLine();
			while(!s.equals("##")){
				words.add(s);
				s = obj.readLine();
			}
			obj.close();
		}
		catch(FileNotFoundException e){
			System.out.println("#File Missing - " + e);
			System.exit(0);
		}
		catch(IOException e){
			System.out.println("#IOExcpetion - " + e);
			System.exit(0);
		}
		System.out.println(" L read - Complete");
		readOnce = true;
	}
	
	/**
	 * Generates a random word from {@code words}. Decides if file to be read based on {@code readOnce}.
	 * <p>Only accessible method of {@code Word} class. Expected to be called from {@code start()} method in {@code Main}.</p>
	 * 
	 * @return The generated random word, for the current game.
	 */
	public static String getWord(){
		System.out.println("Word");
		if(readOnce == false)
			read();
		else
			System.out.println(" L read - Skip");
		int i = new Random().nextInt(words.size());
		System.out.println(" L getWord - Complete");
		System.out.println("\n " + (i + 1) + " of " + words.size() + "\n");
		return words.get(i);
	}
}
