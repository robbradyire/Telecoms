package Overhead;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import tcdIO.Terminal;
import Packets.AcknowledgeSetup;
import Packets.GenericActionPacket;
import Packets.GenericTimedActionPacket;
import Packets.PacketContent;
import Packets.WorkRequest;
import Packets.WorkloadPacket;

/**
 * Client class An instance accepts user input
 */
public class Client extends Node {
	public static final int DEFAULT_DST_PORT = 50001;
	private static final String DEFAULT_DST_NODE = "localhost";
	public Terminal terminal;
	public InetSocketAddress dstAddress;
	private boolean targetFound = false;
	private GenericTimedActionPacket setupRequest;
	private WorkRequest workRequest;
	private GenericActionPacket actionPacket;
	private DataProcessor leDataProcessor;
	private int statsNoOfNames;
	private int statsNoOfWorkloads;
	private String target;

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
		this.statsNoOfNames = 0;
		this.statsNoOfWorkloads = 0;
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		int type = content.getType();
		switch (type) {
			case PacketContent.WORKER_ADDED_ACK:
				setupRequest.confirmSent();
				target = ((AcknowledgeSetup) content).getTarget();
				this.notify();
				break;
			case PacketContent.PING_WORKER:
				actionPacket = new GenericActionPacket(getDestAddress(), this,
						PacketContent.RESPONSE_PING);
				actionPacket.send();
				break;
			case PacketContent.WORKLOAD_PACKET:
				this.statsNoOfWorkloads++;
				leDataProcessor = new DataProcessor(
						((WorkloadPacket) content).getData(), target);
				leDataProcessor.processTheData(this);
				statsNoOfNames += leDataProcessor.getData().length;
				leDataProcessor.processTheData(this);
				terminal.println("Worked through " + statsNoOfNames
						+ " names so far");
				this.notify();
				break;
			case PacketContent.TERMINATE_WORK:
				targetFound = true;
				this.notify();
				break;
		}
	}

	/**
	 * Sender Method
	 */
	public synchronized void start() throws Exception {
		setupRequest = new GenericTimedActionPacket(getDestAddress(), this,
				PacketContent.ADD_WORKER_REQUEST);
		setupRequest.send();
		this.wait();
		terminal.println("Added to list of workers");
		while (!targetFound) {
			workRequest = new WorkRequest(1000, this);
			workRequest.send();
			this.wait();
		}
		terminal.println("Work Completed. \n Your computer's statistics:");
		terminal.println("Total number of names processed: " + statsNoOfNames);
		terminal.println("Total number of workload items given: "
				+ statsNoOfWorkloads);
	}

	/**
	 * getDestAddress
	 * returns the SocketAddress of the Server
	 * 
	 * @return dstAddress: the SocketAddress of the Server
	 */
	public SocketAddress getDestAddress() {
		return dstAddress;
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
				srcAddress += 250;
				srcAssigned = false;
			}
		}

		terminal.println("Program completed");
	}
}