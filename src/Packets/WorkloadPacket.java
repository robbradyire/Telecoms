package Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import Overhead.*;
/**
 * 
 * @author Tomas Barry & Calvin Nolan.
 *
 */
public class WorkloadPacket extends PacketContent {
	private byte[] data;
	private Server controller;
	private InetSocketAddress destAddress;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * WorkloadPacket constructor
	 * 
	 * @param data: byte array of byte arrays represents data to be sent
	 * @param destAddress: workers address for the PingRequest
	 * @param server: Server reference to enable send method in this class
	 */
	public WorkloadPacket(byte[] data, InetSocketAddress destAddress,
			Server server) {
		this.data = data;
		this.type = WORKLOAD_PACKET;
		this.destAddress = destAddress;
		this.controller = server;
	}

	/**
	 * PingRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected WorkloadPacket(ObjectInputStream oin) {
		try {
			this.type = WORKLOAD_PACKET;
			this.data = new byte[oin.available()];
			oin.read(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods
	// -------------------------------------------------------------
	/**
	 * sendWorkload
	 * sends a WorkloadPacket to the worker
	 * 
	 */
	public void sendWorkload() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDestAddress());
			this.controller.socket.send(packet);
		}
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * toObjectOutputStream
	 * writes content into an ObjectOutputStream
	 * 
	 * @param out: output stream to write
	 */
	protected void toObjectOutputStream(ObjectOutputStream out) {
		try {
			this.type = WORKLOAD_PACKET;
			out.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Getters
	// --------------------------------------------------------------
	/**
	 * getDestAddress
	 * returns the destination address of the ping
	 * 
	 * @return destAddress: the destination address of the workload
	 */
	public InetSocketAddress getDestAddress() {
		return destAddress;
	}

	/**
	 * getData
	 * returns data contained in the packet
	 * 
	 * @return data: byte array containing the data
	 */
	public byte[] getData() {
		return this.data;
	}

	// toStrings
	// --------------------------------------------------------------
	/**
	 * toString
	 * returns the data in the packet as String
	 * 
	 * @return names in the byte array in string form.
	 */
	public String toString() {
		return new String(this.data);
	}
}