package Packets;
import Overhead.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;


/**
 * @author Tomas Barry
 * 
 *         PingRequest
 *         Controlled by the Connection class object for the Server
 * 
 */
public class PingRequest extends PacketContent {
	private Server controller;
	private SocketAddress destAddress;
	private Timer timer;
	private boolean pinged;

	/**
	 * PingRequest constructor
	 * 
	 * @param destAddress: workers address for the PingRequest
	 * @param server: Server reference to enable send method in this class
	 */
	public PingRequest(SocketAddress client, Server server) {
		this.type = PING_REQUEST;
		this.destAddress = client;
		this.controller = server;
		this.pinged = false;
	}

	/**
	 * PingRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected PingRequest(ObjectInputStream oin) {
		this.type = PING_REQUEST;
	}

	/**
	 * send
	 * sends a PingRequest to the worker and starts the timer
	 * 
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDestAddress());
			this.controller.socket.send(packet);
			this.timer = new Timer(this);
		}
		catch (SocketException e) {
			// no action
		}
		catch (IOException e) {
			// no action
		}
	}

	/**
	 * tooObjectOutputStream
	 * writes content into an ObjectOutputStream
	 * 
	 * @param out: output stream to write
	 */
	protected void toObjectOutputStream(ObjectOutputStream out) {
		// nothing to write
	}

	/**
	 * confirmPing
	 * Called by the controllers Connection. Indicates that PingRequest
	 * has been responded to and stops the timer
	 */
	public void confirmPing() {
		this.pinged = true;
		this.timer.killThread();
	}

	/**
	 * getDestAddress
	 * returns the destination address of the ping
	 * 
	 * @return destAddress: the destination address of the ping
	 */
	public SocketAddress getDestAddress() {
		return destAddress;
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether ping has been responded to
	 * 
	 * @return true: if ping responded to
	 * @return false: if ping not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.pinged;
	}

	/**
	 * toString
	 * returns status of ping as a string
	 * 
	 * @return "Ping to x has been sent" if the ping has been sent
	 * @return "Ping to x has not been sent" if the ping has not been sent
	 */
	public String toString() {
		return "Ping to " + getDestAddress().toString()
				+ (hasBeenSent() == true ? "has " : "has not ") + "been sent.";
	}

	/**
	 * Timer
	 */
	private class Timer extends AbstractTimer {

		// Constructor
		// -----------------------------------------------------
		public Timer(PingRequest ping) {
			this.packet = ping;
			this.sleepTime = 1000;
			this.thread = new Thread(this);
			this.thread.start();
		}

		// Methods
		// ---------------------------------------------------
		/**
		 * run
		 * Start the timer for the ping
		 * 
		 * @throws SecurityException
		 */
		public void run() throws SecurityException {
			try {
				Thread.sleep(this.sleepTime);
				if (!((PingRequest) this.packet).hasBeenSent()) {
					System.out.println("Ping resent");
					((PingRequest) this.packet).send();
				}
			}
			catch (Exception e) {
				// Thread interrupted
			}
		}

		/**
		 * killThread
		 * End the current thread
		 * 
		 * @throws InterruptedException, caught in run()
		 */
		public void killThread() {
			this.thread.interrupt();
		}
	}
}