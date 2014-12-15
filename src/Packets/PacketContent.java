package Packets;

import Overhead.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

/**
 * The class is the basis for packet contents of various types.
 */
public abstract class PacketContent {
	public static final int SETUP_REQUEST = 0;
	public static final int SETUP_ACK = 1;
	public static final int PING_REQUEST = 2;
	public static final int PING_RESPONSE = 3;
	public static final int WORK_REQUEST = 4;
	public static final int WORKLOAD_PACKET = 5;
	public static final int TASK_COMPLETE = 6;
	public static final int END_ALL_WORK = 7;
	protected int type = 0;
	protected boolean acknowledged = false;

	/**
	 * Constructs an object out of a datagram packet.
	 * 
	 * @param packet
	 *        Packet to analyse.
	 */
	public static PacketContent fromDatagramPacket(DatagramPacket packet) {
		PacketContent content = null;
		try {
			int type;
			byte[] data;
			ByteArrayInputStream bin;
			ObjectInputStream oin;
			data = packet.getData(); // use packet content as seed for stream
			bin = new ByteArrayInputStream(data);
			oin = new ObjectInputStream(bin);
			type = oin.readInt(); // read type from beginning of packet
			switch (type) { // depending on type create content object
				case SETUP_REQUEST:
					content = new SetupPacket(oin);
					break;
				case SETUP_ACK:
					content = new AcknowledgeSetup(oin);
					break;
				case PING_REQUEST:
					content = new GenericActionPacket(oin, PING_REQUEST);
					break;
				case PING_RESPONSE:
					content = new GenericActionPacket(oin, PING_RESPONSE);
					break;
				case WORK_REQUEST:
					content = new WorkRequest(oin);
					break;
				case WORKLOAD_PACKET:
					content = new WorkloadPacket(oin);
					break;
				case TASK_COMPLETE:
					content = new SucessPacket(oin);
					break;
				case END_ALL_WORK:
					content = new GenericActionPacket(oin, END_ALL_WORK);
					break;
			}
			oin.close();
			bin.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * Returns the type of the packet.
	 * 
	 * @return Returns the type of the packet.
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * confirmSent
	 * Called by the sending Node. Indicates that the Packet has successfully
	 * been sent. This is determined by the sending Node
	 */
	protected void confirmSent() {
		this.acknowledged = true;
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether the Packet has been sent successfully
	 * 
	 * @return true: if Packet responded to
	 * @return false: if Packet not yet responded to
	 */
	protected boolean hasBeenSent() {
		return acknowledged;
	}

	/**
	 * toObjectOutputStream
	 * This method is used to transform content into an output stream.
	 * 
	 * @param out: Stream to write the content for the packet to.
	 */
	protected abstract void toObjectOutputStream(ObjectOutputStream out);

	/**
	 * send
	 * sends the Packet to a Node
	 */
	protected abstract void send();

	/**
	 * Returns the content of the object as DatagramPacket.
	 * 
	 * @return Returns the content of the object as DatagramPacket.
	 */
	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;
		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;
			bout = new ByteArrayOutputStream();
			oout = new ObjectOutputStream(bout);
			oout.writeInt(type); // write type to stream
			toObjectOutputStream(oout); // write content to stream depending on
										// type
			oout.flush();
			data = bout.toByteArray(); // convert content to byte array
			packet = new DatagramPacket(data, data.length); // create packet
															// from byte array
			oout.close();
			bout.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return packet;
	}

	/**
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public abstract String toString();
}