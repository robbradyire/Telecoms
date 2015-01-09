package Overhead;

import Packets.GenericTimedActionPacket;
import Packets.PacketContent;

/**
 * ProcessData
 * 
 * @author Tomas Barry
 * 
 */
public class DataProcessor {
	private String[] data;
	private String target;
	private boolean targetFound;

	/**
	 * ProcessData constructor
	 * 
	 * @param data
	 */
	public DataProcessor(byte[] data, String target) {
		this.data = (new String(data)).split("\n");
		this.target = target;
		this.targetFound = false;
	}

	/**
	 * 
	 */
	public void sendSucessPacket(Client worker) {
		GenericTimedActionPacket sucessPacket = new GenericTimedActionPacket(
				worker.getDestAddress(), worker, PacketContent.TASK_COMPLETE);
		sucessPacket.send();
	}

	/**
	 * processTheData
	 * searches the data for the target
	 * 
	 * @param worker
	 * 
	 * @return false: data doesn't contain target
	 * @return true: data does contain target
	 */
	public boolean processTheData(Client worker) {
		for (String s : data) {
			if (s.trim().equals(getTarget())) {
				targetFound = true;
				sendSucessPacket(worker);
				return true;
			}
		}
		return false;
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