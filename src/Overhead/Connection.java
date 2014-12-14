package Overhead;

import Packets.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.LinkedList;

/**
 * TODO
 * 
 * @author Tomas Barry and Robert Brady
 * 
 */
public class Connection {
	private Server controller;
	private LinkedList<SocketAddress> connections; // list of clients
	private LinkedList<PingRequest> pingList;
	private LinkedList<Integer> pingCounts;

	/**
	 * Constructor
	 */
	public Connection(Server controller) {
		this.controller = controller;
		connections = new LinkedList<SocketAddress>();
		pingCounts = new LinkedList<Integer>();
	}

	/**
	 * addConnection
	 * add a Client socket address to the list
	 * 
	 * @param clientSocket: SocketAddress of the Client
	 */
	public void addConnection(SocketAddress clientSocket) {
		connections.add(clientSocket);
		pingCounts.add((Integer) 0);
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
	public synchronized void ping() throws SocketException, IOException, InterruptedException {
		PingRequest ping;
		pingList = new LinkedList<PingRequest>();
		// make a copy of the connections to iterate through
		LinkedList<SocketAddress> connectionsCopy = new LinkedList<SocketAddress>();
		for (SocketAddress client : connections) {
			connectionsCopy.add(client);
		}
		
		int i = 0;
		for (SocketAddress client : connectionsCopy) {
			if ((int) pingCounts.get(i) < 5) {
				ping = new PingRequest(client, controller);
				pingList.add(ping);
				ping.send();
				
				int count = (int) pingCounts.get(i);
				count += 1;
				System.out.println("Count: " + count);
				pingCounts.set(i, (Integer) count);
				i += 1;
			} else {
				System.out.println("Removing connection.");
				connections.remove(i);
				pingCounts.remove(i);
			}
		}
		System.out.println("\n");
	}

	/**
	 * confirmPing
	 * confirm the receipt of a PingResponse
	 * 
	 * 
	 */
	public void confirmPing(SocketAddress client) {
		int i = 0;
		try {
			for (SocketAddress s : connections) {
				if (s.equals(client)) {
					pingList.remove(i).confirmPing();
					pingCounts.set(i, (Integer) 0);
				}
				i++;
			}
		}
		catch (IndexOutOfBoundsException e) {
			// sometimes happens... no need to worry about it
		}
	}

	/**
	 * 
	 */
	public LinkedList<SocketAddress> listConnections() {
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
		for (SocketAddress socket : connections) {
			s.append(socket.toString() + "\n");
		}
		return s.toString();
	}
}