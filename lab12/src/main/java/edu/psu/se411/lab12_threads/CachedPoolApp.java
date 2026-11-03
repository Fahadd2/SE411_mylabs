package edu.psu.se411.lab12_threads;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Same simulation as App, but on a cached thread pool: it creates a new thread
 * whenever no idle thread is available, with no upper limit.
 *
 * Run: clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.CachedPoolApp
 */
public class CachedPoolApp {

	static Logger logger = LoggerFactory.getLogger(CachedPoolApp.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");
		System.out.printf("Cached thread pool (grows as needed)%n%n");

		Simulation.run("Cached pool", Executors.newCachedThreadPool(), 1000, App.NB_TRANSACTIONS, Transaction::new);
		Simulation.run("Cached pool", Executors.newCachedThreadPool(), 10, App.NB_TRANSACTIONS, Transaction::new);

		logger.info("Application is closing...");
	}

}
