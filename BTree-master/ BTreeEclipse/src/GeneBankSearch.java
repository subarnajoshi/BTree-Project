import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Gene Bank Search
 * ----------------
 * Searches a bTree file for sequences.
 * @author Kira Davis
 * @author Nealon Hager
 */
public class GeneBankSearch {
	
	private static File bTreeFile;
	private static File queryFile;
	private static int useCache;
	private static int cacheSize = 0;
	private static int debugLevel;
	
	private static String cmdFormat = "java GeneBankSearch [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]";
	
	public static void main(String[] args) {
		
		// Check for the correct # of args
		if (args.length >= 3 && args.length <= 5) {

			try {
				// Set variables equal to args
				useCache = Integer.parseInt(args[0]); // 0 = no, 1 = yes
				bTreeFile = new File(args[1]);
				queryFile = new File(args[2]);

				// If cache size is set
				if (args.length >= 4)
					cacheSize = Integer.parseInt(args[3]);

				// If debug level is set
				if (args.length == 5)
					debugLevel = Integer.parseInt(args[4]);

				String[] queries;
				queries = parseQueries();
				Integer[] frequencies = new Integer[queries.length];
				for (int i = 0; i < frequencies.length; i++) {
					frequencies[i] = 0;
				}

				String searchString = searchBTree(bTreeFile);
				String asGenome = keyToGenomeString(searchString);

				for (int i = 0; i < asGenome.length(); i++) {
					for (int j = 0; j < queries.length; j++) {
						try {
							if (asGenome.substring(i, i + queries[j].length()).equals(queries[j])) {
								//TODO
								if(queries[j].length()==1)
									frequencies[j]+=2;
								else
									frequencies[j]++;
							}
						} catch (Exception e) {
							break;
						}
					}
				}

				// output frequencies
				for (int i = 0; i < queries.length; i++) {
					System.out.println(frequencies[i] + " " + queries[i]);
				}
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Wrong number of arguments. Format:\n" + cmdFormat);
		}
	} 
	
	/** This method parses the queries and turns them into an array of strings
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String[] parseQueries() throws FileNotFoundException {
		String[] queries;
					
		Scanner scan = new Scanner(queryFile);
		int i = 0;
		while(scan.hasNext()) {
			i++;
			String toss = scan.next();
		}
		
		queries = new String[i];
		
		Scanner scanner = new Scanner(queryFile);
		int j = 0;
		while(scanner.hasNext()) {
			String query = scanner.next();
			query = query.toLowerCase();
			queries[j] = query;
			j++;
		}
		
		return queries;
	}
	
	/** This method searches the BTreeFile and converts it to a string.
	 * @param btreeFile
	 * @return
	 * @throws IOException
	 */
	public static String searchBTree(File btreeFile) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(btreeFile, "r");
		String output = "";
		
		for (int i = 0; i < raf.length(); i++) {
			String b = "" + raf.readByte();
			if(b.equals("49"))
				output+="1";
			if(b.equals("48"))
				output+="0";
			
		}
	
		return output;
	}
	
	/** This method takes a binary value and converts it to a genome sequence
	 * @param input
	 * @return String
	 */
	public static String keyToGenomeString(String input) {
		String output = "";
		for (int i = 0; i < input.length(); i += 2) {
			if (input.substring(i, i + 2).equals("00")) {
				output += "a";
			} else if (input.substring(i, i + 2).equals("11")) {
				output += "t";
			} else if (input.substring(i, i + 2).equals("01")) {
				output += "c";
			} else if (input.substring(i, i + 2).equals("10")) {
				output += "g";
			}
		}
		return output;
	}
}
