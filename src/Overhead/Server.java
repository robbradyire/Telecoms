package Overhead;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import tcdIO.Terminal;
import Packets.AcknowledgeSetup;
import Packets.PacketContent;
import Packets.WorkRequest;
import Packets.WorkloadPacket;

public class Server extends Node {
	//	private String target = "nishant gupta";
	private final String target = "jimmy rustler";
	private static final int DEFAULT_PORT = 50001;
	private boolean taskComplete = false;
	private Terminal terminal;
	private WorkloadPacket workload;
	private Connection connectionList;
	private Thread dataAllocatorThread;
	private DataAllocator dataAllocator;

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
		try {
			if (!taskComplete) {
				PacketContent content = PacketContent
						.fromDatagramPacket(packet);
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
						int requestedSize = work.getCapacity();
						byte[] data = (dataAllocator.getBytes(requestedSize))
								.getBytes();
						connectionList.updateNames(packet.getSocketAddress(),
								data);
						workload = new WorkloadPacket(data,
								packet.getSocketAddress(), this);
						workload.send();
						break;
					case PacketContent.RESPONSE_PING:
						connectionList.confirmPing(packet.getSocketAddress());
						break;
					case PacketContent.TASK_COMPLETE:
						connectionList.terminateWorkers();
						taskComplete = true;
						dataAllocatorThread.interrupt();
						this.notify();
						break;
				}
			}
			else {
				connectionList.terminateWorker(packet.getSocketAddress());
			}
		}
		catch (NullPointerException e) {
		}
	}

	/**
	 * returnNames
	 * when a worker is assumed to no longer be active the data that it was
	 * processing needs to be put back into the dataAllocator queue
	 * 
	 * @param returnedNames: byte array of the names to be added back into the
	 *        dataAllocator queue
	 */
	public void returnNames(byte[] returnedNames) {
		dataAllocator.returnBytes(returnedNames);
	}

	/**
	 * getPortNumber
	 * returns the port of the Server
	 * 
	 * @return dstAddress: the port number of the Server
	 */
	public int getPortNumber() {
		return DEFAULT_PORT;
	}

	/**
	 * start
	 * controls sequence of operations performed by the Server
	 * 
	 * @throws Exception
	 */
	public synchronized void start() throws Exception {
		connectionList = new Connection(this);
		dataAllocatorThread = new Thread(dataAllocator);
		dataAllocatorThread.start();
		while (!taskComplete) {
			this.wait(250);
			connectionList.ping();
			terminal.println("# of workers = "
					+ connectionList.numberOfConnections() + ". "
					+ dataAllocator.getNoOfNames() + "/100,000,000");
		}
	}

	public void print(String s) {
		terminal.println(s);
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