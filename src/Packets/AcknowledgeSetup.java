package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;

import Overhead.Server;

/**
 * 
 * @author Tomas Barry
 * 
 *         AcknowledgeSetup
 * 
 *         Sent from the Server in response to a SetupPacket from a Worker to
 *         let it know that the Server accepts it as a worker and it will send
 *         it data to process upon request
 * 
 */
public class AcknowledgeSetup extends PacketContent {
	private Server server;
	private SocketAddress workerAddress;
	private String target;

	// Constructors
	// -------------------------------------------------------------------------
	/**
	 * AcknowledgeReceipt constructor
	 * 
	 * @param controller: reference to the Server to allow sending of the packet
	 *        within the class
	 * @param dstAddress: the workers address
	 * @param target: the name that is to be searched for in the data sent later
	 */
	public AcknowledgeSetup(Server controller, SocketAddress dstAddress,
			String taget) {
		this.type = SETUP_ACK;
		this.server = controller;
		this.workerAddress = dstAddress;
		this.target = taget;
	}

	/**
	 * SetupPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	public AcknowledgeSetup(ObjectInputStream oin) {
		try {
			type = SETUP_ACK;
			target = oin.readUTF();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods
	// ----------------------------------------------------------------
	/**
	 * send
	 * Acknowledge the receipt of a SetupPacket
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
		try {
			out.writeUTF(target);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getDestAddress
	 * getter for the Workers address
	 * 
	 * @return dstAddress: Workers SocketAddress
	 */
	public SocketAddress getDestAddress() {
		return workerAddress;
	}

	/**
	 * getTarget
	 * returns the target that is to be searched for
	 * 
	 * @return target: the target that is to be searched for
	 */
	public String getTarget() {
		return target;
	}

	// toString
	// --------------------------------------------------------
	/**
	 * toString
	 * returns a String representation of the status of the Packet
	 * 
	 * @return "AcknowledgeSetup packet from x to y"
	 */
	public String toString() {
		return "AcknowledgeSetup packet from " + server.DEFAULT_PORT + " to "
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