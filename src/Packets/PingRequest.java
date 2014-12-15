package Packets;

import Overhead.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * @author Tomas Barry
 * 
 *         PingRequest
 *         Sent from the Server to a Worker. It tells the Worker that it must
 *         return a PingResponse to let the Server know that it is still active
 * 
 */
public class PingRequest extends PacketContent {
	private Server server;
	private SocketAddress workerAddress;
	private boolean pinged;

	// Constructors
	// -------------------------------------------------------------------

	/**
	 * PingRequest constructor
	 * 
	 * @param destAddress: workers address for the PingRequest
	 * @param server: Server reference to enable send method in this class
	 */
	public PingRequest(SocketAddress client, Server server) {
		this.type = PING_REQUEST;
		this.workerAddress = client;
		this.server = server;
		this.pinged = false;
	}

	/**
	 * PingRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected PingRequest(ObjectInputStream oin) {
		this.type = PING_REQUEST;
	}

	// Methods
	// ------------------------------------------------------------------

	/**
	 * send
	 * sends a PingRequest to the worker and starts the Timer
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

	/**
	 * confirmPing
	 * Called by the Servers Connection. Indicates that PingRequest
	 * has been responded to and stops the timer
	 */
	public void confirmPing() {
		this.pinged = true;
	}

	// Getters
	// -------------------------------------------------------------

	/**
	 * getDestAddress
	 * returns the destination address of the ping
	 * 
	 * @return destAddress: the destination address of the ping
	 */
	public SocketAddress getDestAddress() {
		return workerAddress;
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether ping has been responded to
	 * 
	 * @return true: if ping responded to
	 * @return false: if ping not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.pinged;
	}

	// toString
	// ------------------------------------------------------------------------
	/**
	 * toString
	 * returns status of ping as a string
	 * 
	 * @return "Ping to x has been sent" if the ping has been sent
	 * @return "Ping to x has not been sent" if the ping has not been sent
	 */
	public String toString() {
		return "Ping to " + getDestAddress().toString()
				+ (hasBeenSent() == true ? "has " : "has not ") + "been sent.";
	}
}