package Overhead;

import Packets.*;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.LinkedList;

/**
 * TODO
 * 
 * @author Tomas Barry
 * 
 */
public class Connection {
	private Server controller;
	private LinkedList<SocketAddress> connections; // list of clients
	private LinkedList<PingRequest> pingList;

	/**
	 * Constructor
	 */
	public Connection(Server controller) {
		this.controller = controller;
		connections = new LinkedList<SocketAddress>();
	}

	/**
	 * addConnection
	 * add a Client socket address to the list
	 * 
	 * @param clientSocket: SocketAddress of the Client
	 */
	public void addConnection(SocketAddress clientSocket) {
		connections.add(clientSocket);
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
	 * 
	 */
	public void ping() throws SocketException, IOException {
		PingRequest ping;
		pingList = new LinkedList<PingRequest>();
		for (SocketAddress client : connections) {
			ping = new PingRequest(client, controller);
			pingList.add(ping);
			ping.send();
		}
	}

	/**
	 * confirmPing
	 * confirm the receipt of a PingResponse
	 * 
	 * TODO
	 */
	public void confirmPing(SocketAddress client) {
		int i = 0;
		try {
			for (SocketAddress s : connections) {
				if (s.equals(client)) {
					pingList.remove(i).confirmPing();
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