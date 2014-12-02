/**
 * Timer abstract class. Private classes extend from this where it is necessary
 * to have a timer (such as Ping etc)
 */
public abstract class AbstractTimer implements Runnable {
	protected PacketContent packet;
	protected Thread thread;
	protected int sleepTime;

	/**
	 * Start the timer in a new thread
	 */
	public abstract void run();

	/**
	 * End the current thread
	 */
	public abstract void killThread();
}


/**
 * Timer for the FileInfoContent that sleeps the Thread allowing time for an
 * ACK to be received and processed before potentially being resent. The
 * timer is interrupted when the receipt of the object is confirmed
 */
//---------------------------EXAMPLE_USE-------------------------------------
//private class Timer extends AbstractTimer {
//
//	public Timer(FramePacketContent info) {
//		this.packet = info;
//		this.sleepTime = 2 * Client.WINDOW_WAIT_TIME;
//		this.thread = new Thread(this);
//		this.thread.start();
//	}
//
//	/**
//	 * Start the timer in a new thread.
//	 */
//	public void run() {
//		try {
//			Thread.sleep(this.sleepTime);
//			if (!((FramePacketContent) this.packet).hasBeenSent()) {
//				System.out.println("Packet dropped, resending frame "
//						+ ((FramePacketContent) this.packet)
//								.getFrameNumber());
//				((FramePacketContent) this.packet).send();
//			}
//		}
//		catch (Exception e) {
//			// Thread interrupted
//		}
//	}
//
//	/**
//	 * End the current thread
//	 */
//	public void killThread() {
//		this.thread.interrupt();
//	}
//}