import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import tcdIO.Terminal;

public class Server extends Node {
	public static final int DEFAULT_PORT = 50001;
	private Terminal terminal;
	private Connection connectionList;

	/*
	 * constructor gives server a terminal and a socket starting its thread
	 */
	Server(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			this.socket = new DatagramSocket(port);
			this.listener.go();
		}
		catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * called when Server receives a Datagram Packet
	 * 
	 * @param packet
	 *        : the packet received
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		switch (content.getType()) {
			case PacketContent.SETUP_REQUEST:
				connectionList.addConnection(packet.getSocketAddress());
				AcknowledgeSetup ack = new AcknowledgeSetup(this,
						packet.getSocketAddress());
				try {
					System.out.println(connectionList.numberOfConnections());
					ack.send();
				}
				catch (SocketException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case PacketContent.PING_RESPONSE:
				connectionList.confirmPing(packet.getSocketAddress());
				break;
			default:
				break;
		}
		terminal.println(connectionList.toString());
	}

	public synchronized void start() throws Exception {
		connectionList = new Connection(this);
		System.out.println(connectionList.numberOfConnections());
		boolean pinging = false;
		while (!pinging) {
			this.wait(1000);
			connectionList.ping();
		}
	}

	/*
	 * 
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Server");
			(new Server(terminal, DEFAULT_PORT)).start();
			terminal.println("Program completed");
		}
		catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}