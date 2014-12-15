package Overhead;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import tcdIO.Terminal;
import Packets.AcknowledgeSetup;
import Packets.PacketContent;
import Packets.WorkRequest;
import Packets.WorkloadPacket;

public class Server extends Node {
	public static final int DEFAULT_PORT = 50001;
	private Connection connectionList;
	private boolean pinging = false;
	private WorkloadPacket workload;
	private DataAllocator dataAllocator;

	private boolean taskComplete = false;
	private String target = "Justin Beiber";
	private Terminal terminal;

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
		if (!taskComplete) {
			PacketContent content = PacketContent.fromDatagramPacket(packet);
			int type = content.getType();
			switch (type) {
				case PacketContent.ADD_WORKER_REQUEST:
					connectionList.addConnection(packet.getSocketAddress());
					AcknowledgeSetup ack = new AcknowledgeSetup(this,
							packet.getSocketAddress(), target);
					ack.send();
					break;
				case PacketContent.WORKLOAD_REQUEST:
					WorkRequest work = (WorkRequest) content;
					int dataSize = work.getCapacity();
					byte[] data = dataAllocator.getBytes(dataSize);
					workload = new WorkloadPacket(data,
							packet.getSocketAddress(), this);
					workload.send();
					break;
				case PacketContent.RESPONSE_PING:
					connectionList.confirmPing(packet.getSocketAddress());
					System.out.println("confirming ping from "
							+ packet.getSocketAddress());
					break;
				case PacketContent.TASK_COMPLETE:
					connectionList.terminateWorkers();
					taskComplete = true;
					this.notify();
					break;
			}
		}
		else {
			connectionList.terminateWorker(packet.getSocketAddress());
		}
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
			terminal.println(connectionList.toString() + "all de workers");
			terminal.println("$w3g = " + connectionList.numberOfConnections());
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