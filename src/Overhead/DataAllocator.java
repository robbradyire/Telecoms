package Overhead;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author Calvin Nolan, Tomas Barry
 * 
 */

public class DataAllocator {

	ConcurrentLinkedQueue<String> returnedStringQueue;
	ConcurrentLinkedQueue<String> itemsNameQueue;
	
	File file;
	int minimumNameSize;
	int namesProcessed;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * Constructor initializes the pointer and converters the text file into the
	 * string array fileString.
	 */
	public DataAllocator() {

		returnedStringQueue = new ConcurrentLinkedQueue<String>();
		itemsNameQueue = new ConcurrentLinkedQueue<String>();
		
		namesProcessed = 0;
		minimumNameSize = 10;
		String textFile = "names.txt";
		
		file = new File(textFile);
	}
	
	public void processFile(){

		// Converting the text file into the string array fileString.
		try {

			FileInputStream fstream_text = new FileInputStream(file);
			DataInputStream data_input = new DataInputStream(fstream_text);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					data_input));
			String str_line;

			while ((str_line = buffer.readLine()) != null) {
				str_line = str_line.trim();
				if ((str_line.length() != 0)) {
					itemsNameQueue.add(str_line + "\n");
				}
			}
			buffer.close();

		}

		// Catch exception if any
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	// Methods
	// -------------------------------------------------------------
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
	public byte[] getBytes(int noOfBytesWanted) {
		if (noOfBytesWanted > minimumNameSize) {
			
			int currentByteSize = 0;
			boolean isGreatestSize = false;

			String namesToSend = "";

			// Calculates how many names should be given from the queue.
			while (!isGreatestSize && returnedStringQueue.peek() != null) {
				byte[] a = (returnedStringQueue.peek()).getBytes();

				if (currentByteSize + a.length > noOfBytesWanted) {
					isGreatestSize = true;
				}
				else {
					currentByteSize += a.length;
					namesToSend += returnedStringQueue.remove();
					namesProcessed++;
				}
			}

			// Calculates how many names should be given from the file of text.
			while (!isGreatestSize && (itemsNameQueue.peek() != null)) {
				byte[] b = (itemsNameQueue.peek()).getBytes();

				if (currentByteSize + b.length > noOfBytesWanted) {
					isGreatestSize = true;
				}
				else {
					currentByteSize += b.length;
					namesToSend += itemsNameQueue.remove();
					namesProcessed++;
				}
			}

			byte[] bytesToSend = namesToSend.getBytes();

			return bytesToSend;
		}
		return "No Name".getBytes();
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
		String bytesAsString = new String(bytes);

		System.out.println("Returning bytes: \n" + bytesAsString);

		int lastNameIndex = 0;

		// Breaking up the String into the individual names and enqueuing them.
		for (int i = 0; i < bytesAsString.length(); i++) {
			if (bytesAsString.charAt(i) == '\n') {
				returnedStringQueue.add(bytesAsString.substring(lastNameIndex,
						i) + "\n");
				lastNameIndex = i + 1;
				namesProcessed--;
			}
		}
	}

	/**
	 * isCompleteFileSent:
	 * 
	 * Checks to see if the entire file has been distributed, including the data
	 * in the queue.
	 * 
	 * @return true: If the pointer has reached the end of the list of names and
	 *         the queue is empty.
	 */
	public boolean isCompleteFileSent() {
		if (returnedStringQueue.peek() == null)
			return true;
		else
			return false;
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
