import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * PingResponse
 * 
 * @author Tomas Barry
 * 
 */
public class PingResponse extends PacketContent {
	private Client controller;

	/**
	 * constructor
	 */
	public PingResponse(Client responder) {
		this.type = PING_RESPONSE;
		this.controller = responder;
	}

	/**
	 * PingRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected PingResponse(ObjectInputStream oin) {
		this.type = PING_RESPONSE;
	}

	/**
	 * send
	 * sends a PingResponse to the Server
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void send() throws IOException, SocketException {
		DatagramPacket packet = this.toDatagramPacket();
		packet.setSocketAddress(this.getDestAddress());
		this.controller.socket.send(packet);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public InetSocketAddress getDestAddress() {
		return this.controller.dstAddress;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}