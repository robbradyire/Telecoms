package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

import Overhead.Client;

/**
 * 
 * @author Tomas Barry
 * 
 *         PingResponse
 *         Sent by the Worker in response to a PingRequest to let the Server
 *         know that it is still an active Worker
 */
public class PingResponse extends PacketContent {
	private Client worker;

	// Constructors
	// -----------------------------------------------------------

	/**
	 * PingResponse constructor
	 */
	public PingResponse(Client worker) {
		this.type = PING_RESPONSE;
		this.worker = worker;
	}

	/**
	 * PingResponse constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected PingResponse(ObjectInputStream oin) {
		this.type = PING_RESPONSE;
	}

	// Methods
	// -----------------------------------------------------------------

	/**
	 * send
	 * sends a PingResponse to the Server
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(worker.dstAddress);
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
	protected void toObjectOutputStream(ObjectOutputStream out) {
		// nothing to write
	}

	// toString
	// -------------------------------------------------------

	/**
	 * toString
	 * returns a String representation of the status of the PingResponse
	 * 
	 * @return "Response Ping from x to y";
	 */
	public String toString() {
		return "Response Ping from " + worker.toString() + " to "
				+ worker.dstAddress;
	}

}