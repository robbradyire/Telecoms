package Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;

import Overhead.Server;

/**
 * 
 * @author Tomas Barry & Calvin Nolan
 * 
 *         WorkloadPacket
 * 
 *         Sent from the Server in response to a WorkRequest from a Worker
 * 
 */
public class WorkloadPacket extends PacketContent {
	private byte[] data;
	private Server server;
	private SocketAddress workerAddress;

	// Constructors
	// -------------------------------------------------------------
	/**
	 * WorkloadPacket constructor
	 * 
	 * @param data: byte array of byte arrays represents data to be sent
	 * @param destAddress: workers address for the WorkloadPacket
	 * @param server: Server reference to enable send method in this class
	 */
	public WorkloadPacket(byte[] data, SocketAddress destAddress, Server server) {
		this.data = data;
		this.type = WORKLOAD_PACKET;
		this.workerAddress = destAddress;
		this.server = server;
	}

	/**
	 * WorkloadPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected WorkloadPacket(ObjectInputStream oin) {
		try {
			this.type = WORKLOAD_PACKET;
			this.data = new byte[oin.available()];
			oin.read(data);
		}
		catch (Exception e) {
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
			this.server.socket.send(packet);
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Getters
	// --------------------------------------------------------------
	/**
	 * getDestAddress
	 * returns the destination address of the WorkloadPacket
	 * 
	 * @return destAddress: the destination address of the workload
	 */
	public SocketAddress getDestAddress() {
		return workerAddress;
	}

	/**
	 * getData
	 * returns data contained in the packet
	 * 
	 * @return data: byte array containing the data
	 */
	public byte[] getData() {
		return data;
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
		return new String(data);
	}

	@Override
	protected void send() {
		// TODO Auto-generated method stub
		
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