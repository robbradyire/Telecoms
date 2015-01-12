package Overhead;

import Packets.PacketContent;

/**
 * @author Tomas Barry
 * 
 *         Timer
 *         Used in GenericTimedActionPacket as the primary form of controlling
 *         the re-sending of the packet
 */
public class Timer implements Runnable {
	private PacketContent packet;
	private Thread thread;
	private int waitTime;

	// Constructor
	// --------------------------------------------------------
	/**
	 * Timer constructor
	 * 
	 * @param packet: the packet that is to have an associated Timer
	 */
	public Timer(PacketContent packet) {
		this.packet = packet;
		this.waitTime = 150;
		this.thread = new Thread(this);
		this.thread.start();
	}

	// Methods
	// --------------------------------------------------------
	/**
	 * run
	 * Start the Timer
	 */
	public void run() {
		try {
			Thread.sleep(waitTime);
			if (!((PacketContent) packet).hasBeenSent()) {
				((PacketContent) packet).send();
			}
		}
		catch (Exception e) {
			// Thread interrupted by killThread command
		}
	}

	/**
	 * killThread
	 * End the current thread
	 */
	public void killThread() {
		thread.interrupt();
	}
}