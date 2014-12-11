package Packets;
import Overhead.*;
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
	 * @throws SecurityException
	 */
	public abstract void run() throws SecurityException;

	/**
	 * End the current thread
	 */
	public abstract void killThread();
}