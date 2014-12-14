package Overhead;

import Packets.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import tcdIO.Terminal;

public class Server extends Node {
	public static final int DEFAULT_PORT = 50001;
	private Terminal terminal;
	private Connection connectionList;
	private boolean pinging = false;
	private AcknowledgeSetup ack;
	private WorkloadPacket workLoad;
	private TerminateWork terminate;
	private String targetName;

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
		int type = content.getType();
		switch (type) {
			case PacketContent.SETUP_REQUEST:
				connectionList.addConnection(packet.getSocketAddress());
				ack = new AcknowledgeSetup(this, packet.getSocketAddress(),
						targetName);
				ack.send();
				break;
			case PacketContent.WORK_REQUEST:
				WorkRequest work = (WorkRequest) content;
				int dataSize = work.getCapacity();
				// TODO call for data of size capacity
				workLoad = new WorkloadPacket(
						(new DataAllocator().getBytes(dataSize)),
						packet.getSocketAddress(), this);
				break;
			// TODO
			case PacketContent.PING_RESPONSE:
				connectionList.confirmPing(packet.getSocketAddress());
				break;
			case PacketContent.TASK_COMPLETE:
				for (SocketAddress workerAddress : connectionList
						.listConnections()) {
					terminate = new TerminateWork(this, workerAddress);
					terminate.send();
				}
				this.notify();
			default:
				break;
		}
		terminal.println(connectionList.toString());
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