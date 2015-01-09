package Overhead;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import Packets.GenericActionPacket;
import Packets.PacketContent;

/**
 * @author Tomas Barry & Robert Brady
 * 
 *         Connection
 * 
 *         Used by the Server to manage it's connections to Worker Nodes. These
 *         Nodes are kept track of using a ConcurrentHashMap to enable
 *         concurrent modification of the Map the Keys of the Map are the unique
 *         SocketAddresses and the Values of the Map are the counts for the
 *         number of times that Worker has been pinged without response.
 * 
 */
public class Connection {
	private Server server;
	private ConcurrentHashMap<SocketAddress, Integer> connections;
	private int thresholdPing;

	// Constructor
	// ---------------------------------------------------------------------
	/**
	 * Connection onstructor
	 */
	public Connection(Server controller) {
		this.server = controller;
		this.connections = new ConcurrentHashMap<SocketAddress, Integer>();
		this.thresholdPing = 5;
	}

	// Methods
	// ---------------------------------------------------------------------
	/**
	 * addConnection
	 * add a Worker to the Map and initialize its ping count to 0
	 * 
	 * @param clientSocket: SocketAddress of the Worker
	 */
	public void addConnection(SocketAddress clientSocket) {
		connections.put(clientSocket, 0);
	}

	/**
	 * numberOfConnections
	 * gives the number of Workers connected to the Server
	 * 
	 * @return the number of Workers connected to the Server
	 */
	public int numberOfConnections() {
		return connections.size();
	}

	/**
	 * contains
	 * is a worker in the list of workers
	 * return : boolean on whether the worker is in the list
	 */
	public boolean contains(SocketAddress address) {
		return connections.contains(address);
	}

	/**
	 * ping
	 * send a ping to all Workers currently connected to the Server
	 */
	public void ping() {
		GenericActionPacket ping;
		for (SocketAddress worker : connections.keySet()) {
			ping = new GenericActionPacket(worker, server,
					PacketContent.PING_WORKER);
			ping.send();
			connections.put(worker, connections.get(worker) + 1); // update the ping count
			if (connections.get(worker) > thresholdPing) {
				connections.remove(worker);
				System.out.println("Removing " + worker.toString()); // TODO remove before final version
			}
		}
	}

	/**
	 * confirmPing
	 * confirm the receipt of a the Ping from the Worker
	 * 
	 * @param worker: the address of the worker that sent a ping response
	 */
	public void confirmPing(SocketAddress worker) {
		connections.put(worker, 0);
	}

	/**
	 * terminateWorkers
	 * send out a GenericActionPacket to all Workers in the Map indicating that
	 * they can all stop working as the task has been complete or abandoned
	 */
	public void terminateWorkers() {
		for (SocketAddress worker : connections.keySet()) {
			terminateWorker(worker);
		}
	}

	/**
	 * terminateWorker
	 * send out a GenericActionPacket to a Workers in the Map indicating that it
	 * can stop working as the task has been complete or abandoned
	 * 
	 * @param worker: address of the worker to send the Packet to
	 */
	public void terminateWorker(SocketAddress worker) {
		GenericActionPacket terminate;
		terminate = new GenericActionPacket(worker, server,
				PacketContent.TERMINATE_WORK);
		terminate.send();
	}

	/**
	 * getConnections
	 * returns a ConcurrentHashMap object containing the connections
	 * 
	 * @return connections: the connections
	 */
	public ConcurrentHashMap<SocketAddress, Integer> getConnections() {
		return connections;
	}

	// toString
	// ----------------------------------------------------------------
	/**
	 * toString
	 * returns a String representation of the list of connections
	 * 
	 * @return a line separated list of connections
	 */
	public String toString() {
		StringBuilder list = new StringBuilder();
		for (SocketAddress socket : connections.keySet()) {
			list.append(socket.toString() + "\n");
		}
		return list.toString().trim(); // remove newline char from the end
	}
}