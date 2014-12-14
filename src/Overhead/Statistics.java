package Overhead;
import java.util.ArrayList;

import Packets.*;

/* TODO: When adding a new Client, the Server needs to send it it's ID in
 * 		the WorkloadPacket and then when the Client is finished the work,
 * 		it needs to send it's own ID along with it's statistics back to the
 * 		Server so that they can be updated in the server Statistics.
*/

/**
 * Statistics
 * 
 * @author Robert Brady
 *
 */

public class Statistics {
	
	private Server controller;
	private int numberOfWorkers;
	private ArrayList<Integer> numberOfNamesSearched;
	private ArrayList<Timer> timers;
	private ArrayList<Integer> timeSpent;
	private int namesSearchedTotal;
	private double percentOfWorkDone;
	private int namesToSearch;
	
	/**
	 * Statistics
	 * A Class to store and update the Servers statistics
	 * 
	 * @param server
	 */
	public Statistics(Server server) {
		this.controller = server;
		this.numberOfWorkers = 0;
		this.numberOfNamesSearched = new ArrayList<Integer>();
		this.timers = new ArrayList<Timer>();
		this.timeSpent = new ArrayList<Integer>();
		this.namesSearchedTotal = 0;
		this.percentOfWorkDone = 0.0;
		
		namesToSearch = 1000;	// TODO: get the total number of names 
								//		from the controller and put it in here
	}
	
	/**
	 * addWorker()
	 * adds a worker to the statistics, and starts it's Timer
	 * 
	 * @return the ID of the new Worker
	 */
	public int addWorker() {
		int id = numberOfWorkers;
		numberOfNamesSearched.add(0);
		timers.add(new Timer());
		timers.get(id).run();
		timeSpent.add(0);
		numberOfWorkers++;
		
		return id;
	}
	
	/**
	 * updateStats()
	 * Updates the stats for a particular Worker
	 * 
	 * @param id of the worker
	 * @param namesSearched (the number of names that the Worker has searched
	 */
	public void updateStats(int id, int namesSearched) {
		numberOfNamesSearched.set(id, 
				((int)numberOfNamesSearched.get(id)) + namesSearched);
		timers.get(id).killThread();
		namesSearchedTotal += namesSearched;
		percentOfWorkDone += (double) namesSearched / (double) namesToSearch;
	}
	
	/**
	 * resumeWorker()
	 * Resumes the timer for a particular Worker
	 * 
	 * @param id of the Worker
	 */
	public void resumeWorker(int id) {
		timers.get(id).run();
	}
	
	
	
	/**
	 * numberOfNamesSearched()
	 * 
	 * @return the total number of names that have been searched so far
	 */
	public int numberOfNamesSearched() {
		return namesSearchedTotal;
	}
	
	/**
	 * percentageOfWorkDone()
	 * 
	 * @return the percentage of names that have been searched
	 */
	public double percentOfWorkDone() {
		return percentOfWorkDone;
	}
	
	
	/**
	 * Timer
	 */
	private class Timer extends AbstractTimer {
		public Timer() {
			this.thread = new Thread(this);
		}
		
		/*
		 * TODO: need to work out a way to increase the timeSpent for 
		 * 		each timer
		 */

		/**
		 * run
		 * Start the timer
		 * 
		 */
		public void run() {
			this.thread.start();
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