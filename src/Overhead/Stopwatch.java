package Overhead;

/**
 * class taken from princeton
 * 
 *  <i>Stopwatch</i>. This class is a data type for measuring
 *  the running time (wall clock) of a program.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/32class">Section 3.2</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 */



public class Stopwatch {

    private final long start;

   /**
     * Create a stopwatch object.
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    }
    
    public Stopwatch(int i) {
    	start = (long) 0.0;
    }


   /**
     * Return elapsed time (in seconds) since this object was created.
     */
    public double elapsedTime() {
    	if (start == (long) 0.0) {
    		return 0.0;
    	}
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

} 
