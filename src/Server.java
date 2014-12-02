import java.net.DatagramPacket;
import java.net.DatagramSocket;

import tcdIO.Terminal;

public class Server extends Node {
	public static final int DEFAULT_PORT = 50001;
	private Terminal terminal;

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
	 *            : the packet received
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		// TODO
	}

	public synchronized void start() throws Exception {
		// TODO
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