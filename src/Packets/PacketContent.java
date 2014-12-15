package Packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

/**
 * The class is the basis for packet contents of various types.
 */
public abstract class PacketContent {
	public static final int ADD_WORKER_REQUEST = 0;
	public static final int WORKER_ADDED_ACK = 1;
	public static final int PING_WORKER = 2;
	public static final int RESPONSE_PING = 3;
	public static final int WORKLOAD_REQUEST = 4;
	public static final int WORKLOAD_PACKET = 5;
	public static final int TASK_COMPLETE = 6;
	public static final int TERMINATE_WORK = 7;
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
				case ADD_WORKER_REQUEST:
					content = new GenericTimedActionPacket(oin, ADD_WORKER_REQUEST);
					break;
				case WORKER_ADDED_ACK:
					content = new AcknowledgeSetup(oin);
					break;
				case PING_WORKER:
					content = new GenericActionPacket(oin, PING_WORKER);
					break;
				case RESPONSE_PING:
					content = new GenericActionPacket(oin, RESPONSE_PING);
					break;
				case WORKLOAD_REQUEST:
					content = new WorkRequest(oin);
					break;
				case WORKLOAD_PACKET:
					content = new WorkloadPacket(oin);
					break;
				case TASK_COMPLETE:
					content = new GenericTimedActionPacket(oin, TASK_COMPLETE);
					break;
				case TERMINATE_WORK:
					content = new GenericActionPacket(oin, TERMINATE_WORK);
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
	 * Returns the type of the packet.
	 * 
	 * @return Returns the type of the packet.
	 */
	public int getType() {
		return this.type;
	}

	//	/**
	//	 * confirmSent
	//	 * Called by the sending Node. Indicates that the Packet has successfully
	//	 * been sent. This is determined by the sending Node
	//	 */
	//	protected void confirmSent() {
	//		this.acknowledged = true;
	//	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether the Packet has been sent successfully
	 * 
	 * @return true: if Packet responded to
	 * @return false: if Packet not yet responded to
	 */
	protected boolean hasBeenSent() {
		return this.acknowledged;
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
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public abstract String toString();
}