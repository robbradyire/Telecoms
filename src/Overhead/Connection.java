package Overhead;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

import Packets.PingRequest;

/**
 * TODO
 * 
 * @author Tomas Barry and Robert Brady
 * 
 */
public class Connection {
	private Server server;
	private ConcurrentHashMap<SocketAddress, Integer> connections;
	private int thresholdPing;

	/**
	 * Constructor
	 */
	public Connection(Server controller) {
		this.server = controller;
		this.connections = new ConcurrentHashMap<SocketAddress, Integer>();
		this.thresholdPing = 5;
	}

	/**
	 * addConnection
	 * add a Client socket address to the list
	 * 
	 * @param clientSocket: SocketAddress of the Client
	 */
	public void addConnection(SocketAddress clientSocket) {
		// key is the SocketAddress, value is the pingCount, starts at 0
		connections.put(clientSocket, 0);
	}

	/**
	 * numberOfConnections
	 * gives the number of Client connections to the Server
	 * 
	 * @return connections.size(): the size of the list
	 */
	public int numberOfConnections() {
		return connections.size();
	}

	/**
	 * ping
	 * send a ping to all Clients in the lint
	 * 
	 * @throws IOException
	 * @throws SocketException
	 * @throws InterruptedException
	 * 
	 */
	public void ping() {
		PingRequest ping;
		for (SocketAddress worker : connections.keySet()) {
			ping = new PingRequest(worker, server);
			ping.send();
			connections.put(worker, connections.get(worker) + 1);
			if (connections.get(worker) > thresholdPing) {
				connections.remove(worker);
				System.out.println("Removing " + worker.toString());
			}
		}
	}

	/**
	 * confirmPing
	 * confirm the receipt of a PingResponse
	 * 
	 * 
	 */
	public void confirmPing(SocketAddress worker) {
		connections.put(worker, 0);
	}

	/**
	 * 
	 */
	public ConcurrentHashMap<SocketAddress, Integer> listConnections() {
		return connections;
	}

	/**
	 * toString
	 * a String representation of the list of connections
	 * 
	 * @param s.toString(): a line separated list of connections
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (SocketAddress socket : connections.keySet()) {
			s.append(socket.toString() + "\n");
		}
		return s.toString();
	}
}