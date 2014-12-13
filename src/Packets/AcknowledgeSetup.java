package Packets;
import Overhead.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * AcknowledgeReceipt
 * 
 * @author Tomas Barry
 * 
 */
public class AcknowledgeSetup extends PacketContent {
	private Server controller;
	private SocketAddress dstAddress;

	/**
	 * AcknowledgeReceipt constructor
	 * 
	 * @param type
	 * @param controller
	 * @param dstAddress
	 */
	public AcknowledgeSetup(Server controller, SocketAddress dstAddress) {
		this.type = SETUP_ACK;
		this.controller = controller;
		this.dstAddress = dstAddress;
	}

	/**
	 * SetupPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	public AcknowledgeSetup(ObjectInputStream oin) {
		try{
			this.type = SETUP_ACK;
			this.controller = (Server) oin.readObject();
			this.dstAddress = (InetSocketAddress) oin.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * send
	 * Acknowledge the receipt of a SetupPacket
	 * 
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDstAddress());
			this.controller.socket.send(packet);
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
			this.type = SETUP_ACK;
			out.writeObject(controller);
			out.writeObject(dstAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getDstAddress
	 * getter for the Clients address
	 * 
	 * @return dstAddress: Clients SocketAddress
	 */
	public SocketAddress getDstAddress() {
		return dstAddress;
	}

	/**
	 * 
	 */
	public String toString() {
		return null;
	}
}