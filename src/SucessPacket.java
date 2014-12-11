import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

/**
 * @author Tomas Barry
 * 
 *         WorkRequest
 * 
 */
public class SucessPacket extends PacketContent {
	private boolean hasBeenSent;
	private Timer timer;
	private Client controller;

	/**
	 * SucessPacket constructor
	 * 
	 * @param client: reference object to sender of the packet
	 */
	public SucessPacket(Client client) {
		this.type = TASK_COMPLETE;
		this.hasBeenSent = false;
		this.controller = client;
	}

	/**
	 * SucessPacket constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected SucessPacket(ObjectInputStream oin) {
		this.type = TASK_COMPLETE;
	}

	/**
	 * send
	 * sends a message to the Server to let it know that the Client has
	 * completed the objective and start the timer
	 * 
	 */
	public void send() {
		DatagramPacket packet = this.toDatagramPacket();
		try {
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

	/**
	 * toString
	 * returns status of the SetupRequest as a string
	 * 
	 * @return Setup request from x has been acknowledged
	 * @return Setup request from x has not been acknowledged
	 */
	public String toString() {
		return "";
		// TODO
	}

	/**
	 * Timer
	 */
	private class Timer extends AbstractTimer {
		public Timer(SucessPacket work) {
			this.packet = work;
			this.sleepTime = 500;
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
				if (!((SucessPacket) this.packet).hasBeenSent()) {
					System.out.println("SucessPacket resent");
					((SucessPacket) this.packet).send();
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