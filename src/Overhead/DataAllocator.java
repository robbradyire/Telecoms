package Overhead;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Calvin Nolan, Tomas Barry
 * 
 */

public class DataAllocator implements Runnable {
	private ConcurrentLinkedQueue<String> namesQueue;
	private File file;
	private int namesProcessed;
	private int maxQueueSize;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * Constructor initializes the pointer and converters the text file into the
	 * string array fileString.
	 */
	public DataAllocator() {
		namesQueue = new ConcurrentLinkedQueue<String>();
		String textFile = "C:/Users/robert/Documents/names.txt";
		file = new File(textFile);
		namesProcessed = 0;
		maxQueueSize = 100000;
	}

	// Methods
	// -------------------------------------------------------------

	/**
	 * run
	 * method run in a separate thread initiated by the Server. This method runs
	 * through the entire file adding the contents to the queue for allocation
	 * to Workers. The thread will be interrupted if the target name has been
	 * found by a Worker, otherwise it will keep processing the file until it is
	 * done reading the file.
	 */
	public void run() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String str_line;
			while ((str_line = buffer.readLine()) != null) {
				if (namesQueue.size() > maxQueueSize) {
					Thread.sleep(150);
				}
				else {
					namesQueue.add(str_line + "\n");
				}
			}
			buffer.close();
		}
		// Catch exception if any
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * getBytes:
	 * 
	 * Given an integer amount of bytes, the function returns a byte array of
	 * the maximum amount of names from the top of the list of names.
	 * 
	 * @param noOfBytesWanted
	 * @return bytesToSend: The largest amount of names that can be sent under
	 *         the byte limitation given.
	 */
	public String getBytes(int noOfBytesWanted) {
		int currentByteSize = 0;
		StringBuilder namesToSend = new StringBuilder();
		while (currentByteSize < noOfBytesWanted && namesQueue.peek() != null) {
			currentByteSize += namesQueue.peek().length();
			namesToSend.append(namesQueue.remove());
			namesProcessed++;
		}
		return namesToSend.toString();
	}

	/**
	 * returnBytes:
	 * 
	 * Takes in an array of bytes that were given to be processed but never
	 * completed and returns them into a queue to be redistributed before any
	 * new names from the list.
	 * 
	 * @param bytes
	 */
	public void returnBytes(byte[] bytes) {
		String[] bytesAsString = (new String(bytes)).split("\n");
		for (String s : bytesAsString) {
			namesQueue.add(s);
			namesProcessed--;
		}
	}

	// Getter
	// -------------------------------------------------------------

	/**
	 * @return The total number of names to be processed.
	 */
	public int getNoOfNames() {
		return namesProcessed;
	}
}
