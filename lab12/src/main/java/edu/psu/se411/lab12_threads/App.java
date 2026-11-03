package edu.psu.se411.lab12_threads;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Fixed thread pool version.
 * Build and run using Maven goal: clean package exec:java
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static final int NB_TRANSACTIONS = 1_000_000;
	public static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) {
		logger.info("Application is starting...");
		System.out.printf("Fixed thread pool of %d threads (one per CPU core)%n%n", POOL_SIZE);

		Simulation.run("Fixed pool", Executors.newFixedThreadPool(POOL_SIZE), 1000, NB_TRANSACTIONS, Transaction::new);
		Simulation.run("Fixed pool", Executors.newFixedThreadPool(POOL_SIZE), 10, NB_TRANSACTIONS, Transaction::new);

		logger.info("Application is closing...");
	}

}
