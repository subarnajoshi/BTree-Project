import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;

/**
 * Creates a BTree from gbk file data.
 * 
 * Driver class for GeneBankCreateBTree program
 * @author Kira Davis
 * @author Nealon Hager
 */
public class GeneBankCreateBTree {
	
	private static BTree<TreeObject> bTree;
	private static int degree;
	private static int sequenceLength;
	private static int cacheSize;
	private static int debugLevel;
	private static File gbkFile;
	private static String outputFilename;
	private static DataOutputStream os;
	private static String holdSequence;
	private static String placeHolder;
	private static RandomAccessFile raf;
	private static PrintWriter writer;

	private static String cmdFormat = "java GeneBankCreateBTree [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]";
	
	public static void main(String[] args) {
		
		// Check for the correct # of args
		if (args.length >= 3 && args.length <= 6) {
			
			try {
				// Set variables equal to args
				int useCache = Integer.parseInt(args[0]); // 0 = no, 1 = yes
				degree = Integer.parseInt(args[1]);
				int minDegree = degree;
				gbkFile = new File(args[2]);
				sequenceLength = Integer.parseInt(args[3]);
				raf = new RandomAccessFile(gbkFile+".btree.data."+sequenceLength+"."+degree, "rw");
				
				// Invalid args?
				if (useCache < 0 | useCache > 1) {
					System.err.println("Invalid arguments. Format:\n" + cmdFormat);
					return;
				} else if (minDegree < 0 | sequenceLength < 0) {
					System.err.println("No negative values. Format:\n" + cmdFormat);
					return;
				}
				
				// Picking optimum degree
				if (minDegree == 0) {
					boolean foundDegree = false;
					int count = 0;

					// Try degrees until we are >= 4096
					while(count < 4096 && foundDegree == false) {
						
						// My best guess at how much space our nodes take up
						int optDegEquation = ( (sequenceLength*2+8)*(count-1)+8+8*(count-1) );

						// Exceeded 4096, choose one degree less
						if (optDegEquation > 4096) {
							foundDegree = true;
							optDegEquation = ( (sequenceLength*2+8)*(count-2)+8+8*(count-2) );
							minDegree = (count - 2)/2;	
						// Exactly 4096
						} else if (optDegEquation == 4096) {
							foundDegree = true;
							minDegree = (count - 1)/2;
						} else {
							count++;
						}
					}
				}

				// If cache size is set
				if (args.length >= 5) {
					cacheSize = Integer.parseInt(args[4]);
					bTree = new BTree<TreeObject>(minDegree, cacheSize);
				} else {
					bTree = new BTree<TreeObject>(minDegree);
				}
				
				// If debug level is set
				if (args.length == 6)
					debugLevel = Integer.parseInt(args[5]);

				// Generate BTree
				long start = System.nanoTime();
				generateGeneBTree(bTree, sequenceLength, gbkFile); // also writes to file
				long end = System.nanoTime();
				System.err.println("Generating geneBTree took: " + (end - start)/1000000 + " miliseconds");
				
				// Output in order traversal if debug == 1
				if(debugLevel == 1) {
					writer = new PrintWriter("dump.txt", "UTF-8");
					inOrder(bTree.getRoot());
					writer.close();
				}
		
			} catch (Exception e) {
				System.err.println(e);
			}
			
		} else {
			System.err.println("Wrong number of arguments. Format:\n" + cmdFormat);
		}
	} 
	
	/** This method writes into dump.txt an in order traversal of the btree
	 * @param node
	 */
	private static void inOrder(BTreeNode node) {
		if (node.getChild(0) != null)
			inOrder(node.getChild(0));
		for (int i = 0; i < node.getSize(); i++) {
			writer.println((1+node.getObject(i).getFrequency()) + "\t"
					+ (GeneBankSearch.keyToGenomeString(node.getObject(i).getStr())));
		}
		for (int i = 1; i < node.getChildrenArray().length; i++) {
			if (node.getChild(i) != null)
				inOrder(node.getChild(i));
		}
	}
	
	/**
	 * Scans gbk file and generates BTree
	 * @param bTree, sequenceLength, gbkFile
	 * @throws IOException 
	 */
	public static void generateGeneBTree(BTree<TreeObject> bTree, int sequenceLength, File gbkFile) throws IOException {
		
		Scanner scan = new Scanner(gbkFile); 
		boolean fileStart = false;
	
		holdSequence = "";			

		// Scan the whole file
		while (scan.hasNextLine()) { 				
			StringTokenizer st = new StringTokenizer(scan.nextLine());
			
			// Has the file started?
			while (st.hasMoreTokens() && fileStart == false) {
				if(st.nextToken().equals("ORIGIN")) {
					fileStart = true;
					System.out.print(" Inserting.....\n");
				}
			}

			while (scan.hasNext() && fileStart == true) {
				String nextStr = scan.next();
				
				// Has the file ended?
				if (nextStr.equals("//")) {
					fileStart = false;
					System.out.print(" Insertion complete\n");
					break;
				}
				
				// Ignore numbers
				if (!isInt(nextStr)) {
					holdSequence += nextStr;
				}
			}
			
		}

		scan.close();
		
		byte[] array = new byte[holdSequence.length()*2];

		int fails = 0;
		int total = 0;
		for (int i = 0; i < holdSequence.length() - (sequenceLength-1); i += (2*sequenceLength)) {
			total = i;
			String substr = holdSequence.substring(i/2, i/2 + (sequenceLength));
			String substrKey = generateKey(substr);
			TreeObject to = null;
			try {
				to = new TreeObject(Long.parseLong(substrKey)
						,Long.parseLong(substrKey)
						,substrKey);
			} catch (NumberFormatException e1) {
				System.err.println("subsequence length too large, must be <10");
			}
			long key = to.getKey();
			try {
				bTree.Insert(to);
			} catch (Exception e) {				
				System.err.println("Insert " + i + " failed");
				fails++;
			}
			for (int j = 0; j < substrKey.length(); j++) {
				array[i+j] = (byte) substrKey.charAt(j);
			}
		}
		
		if(fails>0)
			System.err.println( String.format("%.4f", ((double) fails/total)*100) + "% insertion fail rate");
		
		try {
			raf.write(array);
		} catch (Exception e) {
			System.err.println("error 3");
		}
	}
	
	/**
	 * Change ACGT base to binary key
	 * @param base
	 * @return key
	 */
	public static String generateKey(String base) {
		String key = "";
		String code = "";
		
		// Convert the gene sequence to a key
		String [] chars = base.split("");
		for(String str:chars) {
			switch(str.toLowerCase()) {
				case "a":	code = "00"; 	// 0b00
							break;
				case "t": 	code = "11";	// 0b11
							break;
				case "c": 	code = "01";	// 0b01
							break;
				case "g": 	code = "10";	// 0b10
							break;
			}
			key += code;
		}

		return key;
	}
	
	/**
	 * Determines if token is int so that we can skip line numbers
	 * in the gkb file.
	 * @param token
	 * @return true or false
	 */
	public static boolean isInt(String token)
	{
	    try
	    {
	        Integer.parseInt(token);
	        return true;
	    } catch (NumberFormatException ex)
	    {
	        return false;
	    }
	}
	
}
