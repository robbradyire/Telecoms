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
	
	private int numberOfWorkers;
	private ArrayList<Integer> numberOfNamesSearched;
	private ArrayList<Stopwatch> timers;
	private ArrayList<Double> timeSpent;
	private int namesSearchedTotal;
	private double percentOfWorkDone;
	private int namesToSearch;
	
	/**
	 * Statistics
	 * A Class to store and update the Servers statistics
	 * 
	 * @param server
	 */
	public Statistics() {
		this.numberOfWorkers = 0;
		this.numberOfNamesSearched = new ArrayList<Integer>();
		this.timers = new ArrayList<Stopwatch>();
		this.timeSpent = new ArrayList<Double>();
		this.namesSearchedTotal = 0;
		this.percentOfWorkDone = 0.0;
		
		namesToSearch = 100000000;
	}
	
	/**
	 * addWorker()
	 * adds a worker to the statistics, and starts it's Timer
	 * 
	 * @return the ID of the new Worker
	 */
	public void addWorker() {
		numberOfNamesSearched.add(0);
		timers.add(new Stopwatch());
		timeSpent.add(0.0);
		numberOfWorkers++;
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
		double time = (double) timers.get(id).elapsedTime();
		timeSpent.set(id, ((double)timeSpent.get(id) + time));
		namesSearchedTotal += namesSearched;
		percentOfWorkDone += (double) namesSearched / (double) namesToSearch;
		this.resumeWorker(id);
	}
	
	/**
	 * resumeWorker()
	 * Resumes the timer for a particular Worker
	 * 
	 * @param id of the Worker
	 */
	public void resumeWorker(int id) {
		timers.set(id, new Stopwatch());
	}
	
	/**
	 * endWorker()
	 * puts an empty timer in the ArrayList of timers
	 * @param id
	 */
	public void endWorker(int id) {
		timers.set(id, new Stopwatch(0));
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
	 * totalTime()
	 * Adds up all of the time that the individual workers have spent
	 * and returns it (in seconds)
	 * 
	 * @return the total time spent on the job
	 */
	public double totalTime() {
		double total = 0.0;
		for (double time : timeSpent) {
			total += (double) time;
		}
		return total;
	}
	
	public String toString() {
		String output = "";
		output += "Total names searched: " + this.numberOfNamesSearched() + "\n";
		output += "Total time spent by workers: " + this.totalTime() + " seconds\n";
		output += "Percentage of names checked: " + (int) this.percentOfWorkDone() + "%";
		
		return output;
	}
}