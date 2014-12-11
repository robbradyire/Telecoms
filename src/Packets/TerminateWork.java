package Packets;
import Overhead.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * @author Robert Brady
 * 
 *         TerminateWork
 * 
 */
public class TerminateWork extends PacketContent {
	Server controller;
	private SocketAddress dstAddress;
	
	/**
	 * TerminateWork constructor
	 * 
	 * @param server: reference object to sender of packet
	 */
	public TerminateWork(Server server, SocketAddress dstAddress) {
		this.type = TASK_COMPLETE;
		this.controller = server;
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
	
	/**
	 * send
	 * sends a message to the Client to let it know that the
	 * job has been finished by a Client.
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
	 * getDstAddress
	 * getter for the Clients address
	 * 
	 * @return dstAddress: Clients SocketAddress
	 */
	public SocketAddress getDstAddress() {
		return dstAddress;
	}
	
	protected void toObjectOutputStream(ObjectOutputStream out) {
		// nothing to write
		
	}
	
	public String toString() {
		return null;
	}
}