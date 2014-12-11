import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import tcdIO.Terminal;

/**
 * Client class An instance accepts user input
 */
public class Client extends Node {
	private static final int DEFAULT_DST_PORT = 50001;
	private static final String DEFAULT_DST_NODE = "localhost";
	private Terminal terminal;
	public InetSocketAddress dstAddress;
	private SetupPacket setupRequest;
	private SucessPacket sucessPacket;

	/**
	 * Constructor Attempts to create socket at given port and create an
	 * InetSocketAddress for the destinations
	 * 
	 * @throws SocketException
	 */
	Client(Terminal terminal, String dstHost, int dstPort, int srcPort)
			throws SocketException {
		this.terminal = terminal;
		this.dstAddress = new InetSocketAddress(dstHost, dstPort);
		this.socket = new DatagramSocket(srcPort);
		this.listener.go();
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		switch (content.getType()) {
			case PacketContent.SETUP_ACK:
				setupRequest.confirmRequest();
				this.notify();
				break;
			case PacketContent.PING_REQUEST:
				PingResponse ping = new PingResponse(this);
				try {
					terminal.println("Sending ping response");
					ping.send();
				}
				catch (SocketException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				confirmSucess();
				this.notify();
				break;
		}
	}

	/**
	 * Sender Method
	 */
	public synchronized void start() throws Exception {
		setupRequest = new SetupPacket(this);
		setupRequest.sendRequest();
		this.wait();
		String dataString = "john smith\ndavid smith\nmichael smith"
				+ "\nchris smith\nmike smith\narun kumar\njames smith\n"
				+ "namit kumar\nimran khan\njason smith\nchris johnson\n"
				+ "jessica smith\nchris brown\nmike jones\nmichael johnson\n"
				+ "mark smith\nsarah smith\nanil kumar\nmanoj kumar\n";
		byte[] data = dataString.getBytes();
		ProcessData process = new ProcessData(data, "sarah smith");
		process.processTheData(this);
		sucessPacket = new SucessPacket(this);
		this.wait();
		sucessPacket.send();
		this.wait();
	}

	/**
	 * TODO
	 * 
	 */
	public void confirmSucess() {
		this.sucessPacket.hasBeenSent();
	}

	/**
	 * Test method Sends a packet to a given address
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Terminal terminal = new Terminal("Client");
		int srcAddress = 50000;
		boolean srcAssigned = false;
		while (!srcAssigned) {
			try {
				(new Client(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT,
						srcAddress)).start();
				srcAssigned = true;
			}
			// srcAddress already in use, assign a new one
			catch (java.net.BindException e) {
				srcAddress += 1000;
				srcAssigned = false;
			}
		}

		terminal.println("Program completed");
	}
}