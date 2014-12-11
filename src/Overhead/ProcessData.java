package Overhead;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import Packets.*;

/**
 * ProcessData
 * 
 * @author Tomas Barry
 * 
 */
public class ProcessData {
	private String[] data;
	private String target;
	private boolean targetFound;

	/**
	 * ProcessData constructor
	 * 
	 * @param data
	 */
	public ProcessData(byte[] data, String target) {
		this.data = (new String(data)).split("\n");
		this.target = target;
		this.targetFound = false;
	}

	/**
	 * processTheData
	 * searches the data for the target
	 * 
	 * @param worker
	 * 
	 * @return 0: data doesn't contain target
	 * @return 1: data does contain target
	 */
	public int processTheData(Client worker) {
		for (String s : data) {
			if (s.equals(getTarget())) {
				targetFound = true;
				return 1;
			}
		}
		return 0;
	}

	/**
	 * getTarget
	 * return String representation of the task target
	 * 
	 * @return target: the name being searched for
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * getData
	 * returns the data being processed
	 * 
	 * @return data: the data being processed
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * foundTarget
	 * return boolean based on whether the task has been complete
	 * 
	 * @return true: task has been complete
	 * @return false: task has not been complete
	 */
	public boolean foundTarget() {
		return targetFound;
	}
}