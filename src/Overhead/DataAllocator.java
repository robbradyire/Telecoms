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

/**
 * 
 * @author Calvin Nolan, Tomas Barry
 * 
 */

public class DataAllocator {

	Queue<String> returnedStringQueue;
	String[] fileString;
	int currentPointer;
	int minimumNameSize;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * Constructor initializes the pointer and converters the text file into the
	 * string array fileString.
	 */
	public DataAllocator() {

		returnedStringQueue = new LinkedList<String>();
		minimumNameSize = 10;
		currentPointer = 0;
		String textFile = "names.txt";
		List<String> itemsName = new ArrayList<String>();

		// Converting the text file into the string array fileString.
		try {
			File file = new File(textFile);

			FileInputStream fstream_text = new FileInputStream(file);
			DataInputStream data_input = new DataInputStream(fstream_text);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					data_input));
			String str_line;

			while ((str_line = buffer.readLine()) != null) {
				str_line = str_line.trim();
				if ((str_line.length() != 0)) {
					itemsName.add(str_line + "\n");
				}
			}
			buffer.close();

			fileString = (String[]) itemsName.toArray(new String[itemsName
					.size()]);

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
			int prevPointer = currentPointer;

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
				}
			}

			// Calculates how many names should be given from the file of text.
			while (!isGreatestSize && currentPointer < fileString.length) {
				byte[] b = fileString[currentPointer].getBytes();

				if (currentByteSize + b.length > noOfBytesWanted) {
					isGreatestSize = true;
				}
				else {
					currentByteSize += b.length;
					currentPointer++;
				}
			}

			// Converts the names to send into a byte array.
			for (int i = 0; prevPointer + i < currentPointer; i++) {
				namesToSend += fileString[prevPointer + i];
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
		if (currentPointer == fileString.length
				&& returnedStringQueue.peek() == null)
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
		return fileString.length;
	}
}
