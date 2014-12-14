package Overhead;

import Packets.*;
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
	public static final int DEFAULT_DST_PORT = 50001;
	private static final String DEFAULT_DST_NODE = "localhost";
	private Terminal terminal;
	public InetSocketAddress dstAddress;
	private String targetName;
	private boolean targetFound = false;
	private SetupPacket setupRequest;
	private SucessPacket sucessPacket;
	private WorkRequest workRequest;
	private PingResponse ping;
	private ProcessData leDataProcessor;

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
		int type = content.getType();
		switch (type) {
			case PacketContent.SETUP_ACK:
				setupRequest.confirmRequest();
				targetName = ((AcknowledgeSetup) content).getTarget();
				this.notify();
				break;
			case PacketContent.PING_REQUEST:
				terminal.println("Sending ping response");
				ping = new PingResponse(this);
				ping.send();
				break;
			case PacketContent.WORKLOAD_PACKET:
				leDataProcessor = new ProcessData(
						((WorkloadPacket) content).getData(), targetName);
				leDataProcessor.processTheData(this);
				this.notify();
				break;
			case PacketContent.END_ALL_WORK:
				targetFound = true;
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
		int capacity = 100;
		while (!targetFound) {
			workRequest = new WorkRequest(capacity, this);
			workRequest.sendRequest();
			this.wait();
		}
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