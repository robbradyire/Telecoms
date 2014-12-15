package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;

import Overhead.Server;

/**
 * @author Robert Brady
 * 
 *         TerminateWork
 *         Sent from the Server to all workers in it's list to tell them that
 *         they can stop their work as the task has been completed
 * 
 */
public class TerminateWork extends PacketContent {
	Server server;
	private SocketAddress dstAddress;

	// Constructors
	// -------------------------------------------------------------------
	/**
	 * TerminateWork constructor
	 * 
	 * @param server: reference object to sender of packet
	 * @param dstAddress: address of the worker to receive the Packet
	 */
	public TerminateWork(Server server, SocketAddress dstAddress) {
		this.type = TASK_COMPLETE;
		this.server = server;
		this.dstAddress = dstAddress;
	}

	/**
	 * TerminateWork constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected TerminateWork(ObjectInputStream oin) {
		this.type = END_ALL_WORK;

	}

	// Methods
	// -------------------------------------------------------------
	/**
	 * send
	 * sends the packet from the Server to the worker
	 * 
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDestAddress());
			this.server.socket.send(packet);
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
		// nothing to write

	}

	// Getters
	// -------------------------------------------------------
	/**
	 * getDestAddress
	 * getter for the Clients address
	 * 
	 * @return dstAddress: Clients SocketAddress
	 */
	public SocketAddress getDestAddress() {
		return dstAddress;
	}

	// toString
	// ----------------------------------------------------------

	/**
	 * toString
	 * returns a String representation of the status of the Packet
	 * 
	 * @return "TerminateWork sent from x to y"
	 */
	public String toString() {
		return "TerminateWork sent from " + server.DEFAULT_PORT + " to "
				+ getDestAddress();
	}

	@Override
	protected void confirmSent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean hasBeenSent() {
		// TODO Auto-generated method stub
		return false;
	}
}