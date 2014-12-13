package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import Overhead.Client;

/**
 * @author Tomas Barry
 * 
 *         WorkRequest
 * 
 */
public class WorkRequest extends PacketContent {
	private int capacity;
	private boolean hasBeenSent;
	private Timer timer;
	private Client controller;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * WorkRequest constructor
	 * 
	 * @param srcAddress: workers address for the WorkRequest
	 * @param capacity: workload that the worker can handle
	 */
	public WorkRequest(int capacity, Client client) {
		this.type = WORK_REQUEST;
		this.capacity = capacity;
		this.hasBeenSent = false;
		this.controller = client;
	}

	/**
	 * WorkRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected WorkRequest(ObjectInputStream oin) {
		try {
			this.type = WORK_REQUEST;
			this.capacity = oin.readInt();
		}
		catch (IOException e) {
			System.out.println("Error in WorkRequest oin constructor");
		}
	}

	// Methods
	// -------------------------------------------------------------
	/**
	 * sendPing
	 * sends a PingRequest to the worker and starts the timer
	 * 
	 */
	public void sendRequest() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.controller.dstAddress);
			this.controller.socket.send(packet);
			this.timer = new Timer(this);
		}
		catch (SocketException e) {
			// no action
		}
		catch (IOException e) {
			// no action
		}
	}

	/**
	 * tooObjectOutputStream
	 * writes content into an ObjectOutputStream
	 * 
	 * @param out: output stream to write
	 */
	protected void toObjectOutputStream(ObjectOutputStream out) {
		// not sure if needed
	}

	/**
	 * confirmRequest
	 * Indicates that WorkRequest has been responded to and stops the timer
	 */
	public void confirmRequest() {
		this.hasBeenSent = true;
		this.timer.killThread();
	}

	// Getters
	// --------------------------------------------------------------
	/**
	 * getCapacity
	 * returns the capacity that the worker has requested
	 * 
	 * @return capacity: the capacity that the worker can handle
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether WorkRequest has been responded to
	 * 
	 * @return true: if WorkRequest responded to
	 * @return false: if WorkRequest not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.hasBeenSent;
	}

	/**
	 * toString
	 * returns status of the WorkRequest as a string
	 * 
	 * @return "Request of size n to x from y"
	 */
	public String toString() {
		return "Request of size " + capacity + "to "
				+ controller.DEFAULT_DST_PORT + "from " + controller.dstAddress;
	}

	/**
	 * Timer for the WorkRequest.
	 */
	private class Timer extends AbstractTimer {

		// Constructor
		// -----------------------------------------------------
		public Timer(WorkRequest work) {
			this.packet = work;
			this.sleepTime = 500;
			this.thread = new Thread(this);
			this.thread.start();
		}

		// Methods
		// ---------------------------------------------------
		/**
		 * run
		 * Start the timer for the WorkRequest
		 * 
		 * @throws SecurityException
		 */
		public void run() throws SecurityException {
			try {
				Thread.sleep(this.sleepTime);
				if (!((WorkRequest) this.packet).hasBeenSent()) {
					System.out.println("Work request resent");
					((WorkRequest) this.packet).sendRequest();
				}
			}
			catch (Exception e) {
				// Thread interrupted
			}
		}

		/**
		 * killThread
		 * End the current thread
		 * 
		 * @throws InterruptedException, caught in run()
		 */
		public void killThread() {
			this.thread.interrupt();
		}
	}
}