import java.io.IOException;
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
public class WorkRequest extends PacketContent {
	private final int capacity;
	private InetSocketAddress srcAddress;
	private InetSocketAddress destAddress;
	private boolean hasBeenSent;
	private Timer timer;
	private Client controller;

	// Constructor
	// -------------------------------------------------------------
	/**
	 * WorkRequest constructor
	 * 
	 * @param srcAddress: workers address for the WorkRequest
	 * @param capacity: workload that the worker can handle
	 */
	public WorkRequest(InetSocketAddress srcAddress,
			InetSocketAddress destAddress, int capacity, Client client) {
		this.type = WORK_REQUEST;
		this.capacity = capacity;
		this.srcAddress = srcAddress;
		this.hasBeenSent = false;
		this.controller = client;
		this.destAddress = destAddress;
	}

	// Methods
	// -------------------------------------------------------------
	/**
	 * sendPing
	 * sends a PingRequest to the worker and starts the timer
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void sendRequest() throws IOException, SocketException {
		DatagramPacket packet = this.toDatagramPacket();
		packet.setSocketAddress(this.getDestAddress());
		this.controller.socket.send(packet);
		this.timer = new Timer(this);
	}

	/**
	 * tooObjectOutputStream
	 * writes content into an ObjectOutputStream
	 * 
	 * @param out: output stream to write
	 */
	protected void toObjectOutputStream(ObjectOutputStream out) {
		// not sure if needed
	}

	/**
	 * confirmRequest
	 * Indicates that WorkRequest has been responded to and stops the timer
	 */
	public void confirmRequest() {
		this.hasBeenSent = true;
		this.timer.killThread();
	}

	// Getters
	// --------------------------------------------------------------
	/**
	 * getCapacity
	 * returns the capacity that the worker has requested
	 * 
	 * @return capacity: the capacity that the worker can handle
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * getDestAddress
	 * returns the destination address of the WorkloaRequest
	 * 
	 * @return destAddress: the destination address of the WorkloadRequest
	 */
	public InetSocketAddress getDestAddress() {
		return destAddress;
	}

	/**
	 * getSrcAddress
	 * returns the destination address of the WorkloadPacket
	 * 
	 * @return destAddress: the destination address of the WorkloadPacket
	 */
	public InetSocketAddress getSrcAddress() {
		return srcAddress;
	}

	/**
	 * hasBeenSent
	 * returns boolean based on whether WorkRequest has been responded to
	 * 
	 * @return true: if WorkRequest responded to
	 * @return false: if WorkRequest not yet responded to
	 */
	public boolean hasBeenSent() {
		return this.hasBeenSent;
	}

	// toStrings TODO
	// --------------------------------------------------------------
	/**
	 * toString
	 * returns status of the WorkRequest as a string
	 * 
	 * @return "Request of size n to x from y"
	 */
	public String toString() {
		return "Request of size " + capacity + "to "
				+ getDestAddress().toString() + "from "
				+ getSrcAddress().toString();
	}

	/**
	 * Timer for the WorkRequest.
	 */
	private class Timer extends AbstractTimer {

		// Constructor
		// -----------------------------------------------------
		public Timer(WorkRequest work) {
			this.packet = work;
			this.sleepTime = 500;
			this.thread = new Thread(this);
			this.thread.start();
		}

		// Methods
		// ---------------------------------------------------
		/**
		 * run
		 * Start the timer for the WorkRequest
		 * 
		 * @throws SecurityException
		 */
		public void run() throws SecurityException {
			try {
				Thread.sleep(this.sleepTime);
				if (!((WorkRequest) this.packet).hasBeenSent()) {
					System.out.println("Work request resent");
					((WorkRequest) this.packet).sendRequest();
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