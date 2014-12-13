package Packets;
import Overhead.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author Tomas Barry
 * 
 *         WorkRequest
 * 
 */
public class SetupPacket extends PacketContent {
	private boolean hasBeenSent;
	private Timer timer;
	private Client controller;

	/**
	 * SetupPacket constructor
	 * 
	 * @param client: reference object to sender of the packet
	 */
	public SetupPacket(Client client) {
		this.type = SETUP_REQUEST;
		this.hasBeenSent = false;
		this.controller = client;
	}

	/**
	 * SetupPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected SetupPacket(ObjectInputStream oin) {
		try {
			this.type = SETUP_REQUEST;
			this.controller = (Client) oin.readObject();
			this.hasBeenSent = oin.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * send
	 * sends a request to set up a connection to the Server and start the timer
	 * 
	 */
	public void sendRequest() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.controller.dstAddress);
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

		try {
			this.type = WORKLOAD_PACKET;
			out.writeObject(controller);
			out.writeBoolean(hasBeenSent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * confirmRequest
	 * Indicates that SetupRequest has been responded to and stops the timer
	 */
	public void confirmRequest() {
		this.hasBeenSent = true;
		this.timer.killThread();
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether WorkRequest has been responded to
	 * 
	 * @return true: if SetupRequest responded to
	 * @return false: if WorkRequest not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.hasBeenSent;
	}

	/**
	 * toString
	 * returns status of the SetupRequest as a string
	 * 
	 * @return Setup request from x has been acknowledged
	 * @return Setup request from x has not been acknowledged
	 */
	public String toString() {
		return "Setup request from " + controller.socket.toString() + " has "
				+ (hasBeenSent() ? " been" : " not been") + " acknowledged";
	}

	/**
	 * Timer
	 */
	private class Timer extends AbstractTimer {
		public Timer(SetupPacket work) {
			this.packet = work;
			this.sleepTime = 1000;
			this.thread = new Thread(this);
			this.thread.start();
		}

		/**
		 * run
		 * Start the timer
		 * 
		 * @throws SecurityException
		 */
		public void run() throws SecurityException {
			try {
				Thread.sleep(this.sleepTime);
				if (!((SetupPacket) this.packet).hasBeenSent()) {
					System.out.println("Setup request resent");
					((SetupPacket) this.packet).sendRequest();
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