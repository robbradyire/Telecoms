import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import tcdIO.Terminal;

/**
 * Client class An instance accepts user input
 */
public class Client extends Node {
	private static int DEFAULT_SRC_PORT = 50000;
	private static final int DEFAULT_DST_PORT = 50001;
	private static final String DEFAULT_DST_NODE = "localhost";
	private Terminal terminal;
	public InetSocketAddress dstAddress;

	/**
	 * Constructor Attempts to create socket at given port and create an
	 * InetSocketAddress for the destinations
	 */
	Client(Terminal terminal, String dstHost, int dstPort, int srcPort)
			throws java.net.BindException {
		boolean srcAssigned = false;
		while (!srcAssigned) {
			try {
				this.terminal = terminal;
				this.dstAddress = new InetSocketAddress(dstHost, dstPort);
				this.socket = new DatagramSocket(srcPort);
				srcAssigned = true;
				this.listener.go();
			}
			// srcPort in use, need to change
			catch (java.lang.Exception e) {
				srcPort += 1000;
				srcAssigned = false;
			}
		}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		// TODO
	}

	/**
	 * Sender Method
	 */
	public synchronized void start() throws Exception {
		// TODO
	}

	/**
	 * Test method Sends a packet to a given address
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Terminal terminal = new Terminal("Client");
		try {
			(new Client(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT,
					DEFAULT_SRC_PORT)).start();
		}
		catch (java.net.BindException e) {
			// Client already running with DEFAULT_SRC_PORT
			// Handled in Client constructor to allow multiple Clients at one time
		}

		terminal.println("Program completed");
	}
}