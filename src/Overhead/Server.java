package Overhead;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

import tcdIO.Terminal;
import Packets.AcknowledgeSetup;
import Packets.GenericActionPacket;
import Packets.PacketContent;
import Packets.WorkRequest;
import Packets.WorkloadPacket;

public class Server extends Node {
	public static final int DEFAULT_PORT = 50001;
	private Terminal terminal;
	private Connection connectionList;
	private boolean pinging = false;
	private AcknowledgeSetup ack;
	private WorkloadPacket workLoad;
	private GenericActionPacket terminationPacket;
	private DataAllocator dataAllocator;

	private String target = "Justin Beiber";

	/*
	 * constructor gives server a terminal and a socket starting its thread
	 */
	Server(Terminal terminal, int port) {
		try {
			this.dataAllocator = new DataAllocator();
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
		int type = content.getType();
		switch (type) {
			case PacketContent.SETUP_REQUEST:
				connectionList.addConnection(packet.getSocketAddress());
				ack = new AcknowledgeSetup(this, packet.getSocketAddress(),
						target);
				ack.send();
				break;
			case PacketContent.WORK_REQUEST:
				WorkRequest work = (WorkRequest) content;
				int dataSize = work.getCapacity();
				byte[] data = dataAllocator.getBytes(dataSize);
				workLoad = new WorkloadPacket(data, packet.getSocketAddress(),
						this);
				break;
			case PacketContent.PING_RESPONSE:
				connectionList.confirmPing(packet.getSocketAddress());
				System.out.println("confirming ping from "
						+ packet.getSocketAddress());
				break;
			case PacketContent.TASK_COMPLETE:
				for (SocketAddress workerAddress : connectionList
						.listConnections().keySet()) {
					terminationPacket = new GenericActionPacket(workerAddress,
							this, PacketContent.END_ALL_WORK);
					terminationPacket.send();
				}
				this.notify();
				break;
			default:
				break;
		}
		terminal.println(connectionList.toString() + "all de workers");
		terminal.println("$w3g = " + connectionList.numberOfConnections());
	}

	/**
	 * start
	 * controls sequence of operations performed by the Server
	 * 
	 * @throws Exception
	 */
	public synchronized void start() throws Exception {
		connectionList = new Connection(this);
		pinging = false;
		while (!pinging) {
			this.wait(2000);
			connectionList.ping();
		}
		System.out.println("Shouldn't get here");
	}

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