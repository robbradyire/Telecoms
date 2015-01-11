package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
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
 *         This class extends GenericActionPacket. The only differences between
 *         this class and the super class are the Timer class variable, the send
 *         method that initiates the Timer and the confirmSent method that
 *         interrupts the Timer
 * 
 */
public class GenericTimedActionPacket extends GenericActionPacket {
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
		super(receiver, sender, type);
	}

	/**
	 * GenericTimedActionPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	public GenericTimedActionPacket(ObjectInputStream oin, int type) {
		super(oin, type);
	}

	// Methods
	// -------------------------------------------------------------------
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
	 * confirmSent
	 * Called by the sending Node. Indicates that the Packet has successfully
	 * been sent. This is determined by the sending Node
	 */
	public void confirmSent() {
		this.acknowledged = true;
		this.timer.killThread();
	}
}