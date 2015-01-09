package Packets;

import Overhead.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

/**
 * @author Tomas Barry
 * 
 *         SucessPacket
 *         Sent from a Worker to the Server to indicate that it has complete the
 *         task assigned by the Server. It expects a TerminateWork packet in
 *         response otherwise it will continue to sent the SucessPacket
 */
public class SucessPacket extends PacketContent {
	private boolean hasBeenSent;
	private AbstractTimer timer;
	private Client worker;

	// Constructors
	// -------------------------------------------------------------
	/**
	 * SucessPacket constructor
	 * 
	 * @param client: reference object to sender of the packet
	 */
	public SucessPacket(Client client) {
		this.type = TASK_COMPLETE;
		this.hasBeenSent = false;
		this.worker = client;
	}

	/**
	 * SucessPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected SucessPacket(ObjectInputStream oin) {
		this.type = TASK_COMPLETE;
	}

	// Methods
	// ---------------------------------------------------------------------
	/**
	 * send
	 * sends a message to the Server to let it know that the Client has
	 * completed the objective and start the timer
	 * 
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.worker.dstAddress);
			this.worker.socket.send(packet);
			this.timer = new AbstractTimer(this);
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
	 * confirmReciept
	 * Indicates that SucessPacket has been responded to and stops the timer
	 */
	public void confirmReciept() {
		this.hasBeenSent = true;
		this.timer.killThread();
	}

	// Getters
	// ----------------------------------------------------------------------

	/**
	 * hasBeenSent
	 * returns boolean based on whether SucessPacket has been responded to
	 * 
	 * @return true: if SucessPacket responded to
	 * @return false: if SucessPacket not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.hasBeenSent;
	}

	// toString
	// ----------------------------------------------------------------------
	/**
	 * toString
	 * returns status of the SetupRequest as a string
	 * 
	 * @return SucessPacket from x has been acknowledged
	 * @return SucessPacket from x has not been acknowledged
	 */
	public String toString() {
		return "SucessPacket from " + worker.socket.toString() + " has "
				+ (hasBeenSent() ? " been" : " not been") + " acknowledged";
	}

//	/**
//	 * Timer
//	 */
//	private class Timer extends AbstractTimer {
//		public Timer(SucessPacket work) {
//			this.packet = work;
//			this.sleepTime = 500;
//			this.thread = new Thread(this);
//			this.thread.start();
//		}
//
//		/**
//		 * run
//		 * Start the timer
//		 * 
//		 * @throws SecurityException
//		 */
//		public void run() throws SecurityException {
//			try {
//				Thread.sleep(this.sleepTime);
//				if (!((SucessPacket) this.packet).hasBeenSent()) {
//					System.out.println("SucessPacket resent");
//					((SucessPacket) this.packet).send();
//				}
//			}
//			catch (Exception e) {
//				// Thread interrupted
//			}
//		}
//
//		/**
//		 * killThread
//		 * End the current thread
//		 * 
//		 * @throws InterruptedException, caught in run()
//		 */
//		public void killThread() {
//			this.thread.interrupt();
//		}
//	}

	@Override
	protected void confirmSent() {
		// TODO Auto-generated method stub
		
	}
}