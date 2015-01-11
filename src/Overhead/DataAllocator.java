package Overhead;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Calvin Nolan, Tomas Barry
 * 
 */

public class DataAllocator implements Runnable {
	ConcurrentLinkedQueue<String> namesQueue;
	File file;
	int namesProcessed;
	int maxQueueSize;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * Constructor initializes the pointer and converters the text file into the
	 * string array fileString.
	 */
	public DataAllocator() {
		namesQueue = new ConcurrentLinkedQueue<String>();
		String textFile = "names.txt";
		file = new File(textFile);
		namesProcessed = 0;
		maxQueueSize = 100000;
	}

	// Methods
	// -------------------------------------------------------------

	public void run() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String str_line;
			while ((str_line = buffer.readLine()) != null) {
				if (namesQueue.size() > maxQueueSize) {
					Thread.sleep(150);
					System.out.println("Thread sleeping");
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
		// Calculates how many names should be given from the queue.
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
		// Convert the bytes to a String.
		String[] bytesAsString = (new String(bytes)).split("\n");
		System.out.println("Returning bytes: \n" + bytesAsString);
		for (String s : bytesAsString) {
			namesQueue.add(s);
		}
	}

	// Getter
	// -------------------------------------------------------------

	/**
	 * 
	 * @return The total number of names to be processed.
	 */
	public int getNoOfNames() {
		return namesProcessed;
	}
}
