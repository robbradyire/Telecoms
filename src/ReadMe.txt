package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;

import Overhead.Node;

/**
 * @author Tomas Barry
 * 
 *         GenericTimedActionPacket
 * 
 *         A packet that sends info either from one Node to another Node. The
 *         packet indicates that some action must be taken by the receiver. It
 *         does make use of a Timer to operate.
 * 
 */
public class GenericTimedActionPacket extends PacketContent {
	private Node sender;
	private SocketAddress destAddress;
	private Timer timer;

	// Constructors
	// -------------------------------------------------------------------

	/**
	 * GenericTimedActionPacket constructor
	 * 
	 * @param receiver: destination address of the packet
	 * @param sender: reference to the sender Node to allow internal sending
	 * @param type: the type of the packet
	 */
	public GenericTimedActionPacket(SocketAddress receiver, Node sender,
			int type) {
		this.type = type;
		this.destAddress = receiver;
		this.sender = sender;
	}

	/**
	 * GenericTimedActionPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	public GenericTimedActionPacket(ObjectInputStream oin, int type) {
		this.type = type;
	}

	// Methods
	// ------------------------------------------------------------------

	/**
	 * send
	 * sends the Packet to a Node
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDestAddress());
			this.sender.socket.send(packet);
			this.timer = new Timer(this);
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
	public void toObjectOutputStream(ObjectOutputStream out) {
		// nothing to write
	}

	/**
	 * confirmSent
	 * Called by the sending Node. Indicates that the Packet has successfully
	 * been sent. This is determined by the sending Node
	 */
	public void confirmSent() {
		this.acknowledged = true;
		this.timer.killThread();
	}

	// Getters
	// -------------------------------------------------------------

	/**
	 * getDestAddress
	 * returns the destination address of the Packet
	 * 
	 * @return destAddress: the destination address of the Packet
	 */
	public SocketAddress getDestAddress() {
		return destAddress;
	}

	/**
	 * getSenderAddress
	 * returns the sender address of the Packet
	 * 
	 * @return senderAddress: the address of the sending Node of the Packet
	 */
	public SocketAddress getSenderAddress() {
		return sender.socket.getLocalSocketAddress();
	}

	// toString
	// ------------------------------------------------------------------------
	/**
	 * toString
	 * returns status of Packet as a string
	 * 
	 * @return "GenericActionPacket to x has been sent": the Packet has been
	 *         sent
	 * @return "GenericActionPacket to x has not been sent" the Packet has not
	 *         been sent
	 */
	public String toString() {
		return "GenericActionPacket to " + getDestAddress().toString()
				+ (hasBeenSent() ? " has " : " has not ") + "been sent.";
	}
}