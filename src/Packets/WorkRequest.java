package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

import Overhead.Client;

/**
 * @author Tomas Barry
 * 
 *         WorkRequest
 * 
 *         Sent by the Workers to the Server to request a WorkloadPacket of a
 *         specific size. It has a built in Timer that will re send the
 *         WorkRequest if the Server does not return a WorkloadPacket in time
 * 
 */
public class WorkRequest extends PacketContent {
	private int capacity;
	private boolean hasBeenSent;
	private Client worker;

	// Constructors
	// -------------------------------------------------------------
	/**
	 * WorkRequest constructor
	 * 
	 * @param capacity: workload that the worker can handle
	 * @param client: reference to the worker who is sending the packet
	 */
	public WorkRequest(int capacity, Client client) {
		this.type = WORKLOAD_REQUEST;
		this.capacity = capacity;
		this.hasBeenSent = false;
		this.worker = client;
	}

	/**
	 * WorkRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected WorkRequest(ObjectInputStream oin) {
		try {
			type = WORKLOAD_REQUEST;
			capacity = oin.readInt();
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
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.worker.dstAddress);
			this.worker.socket.send(packet);
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
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeInt(capacity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * confirmRequest
	 * Indicates that WorkRequest has been responded to and stops the timer
	 */
	public void confirmRequest() {
		this.hasBeenSent = true;
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

	// toString
	// ------------------------------------------------------------------------

	/**
	 * toString
	 * returns status of the WorkRequest as a string
	 * 
	 * @return "Request of size n to x from y"
	 */
	public String toString() {
		return "Request of size " + getCapacity() + "to "
				+ worker.DEFAULT_DST_PORT + "from " + worker.dstAddress;
	}
}