package edu.psu.se411.lab12_threads;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.Transaction;
import edu.psu.se411.lab12_threads.model.UnorderedTransaction;

/**
 * "What could go wrong?" Locks taken in sender-then-receiver order deadlock,
 * then the same run with resource ordering completes.
 *
 * Run: clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.DeadlockDemo
 */
public class DeadlockDemo {

	static Logger logger = LoggerFactory.getLogger(DeadlockDemo.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		System.out.println("=== 1) Locks WITHOUT ordering (sender first, then receiver) ===");
		Simulation.run("Unordered locks", Executors.newFixedThreadPool(App.POOL_SIZE), 10, App.NB_TRANSACTIONS,
				UnorderedTransaction::new);

		System.out.println("=== 2) Locks WITH resource ordering (smallest account id first) ===");
		Simulation.run("Ordered locks", Executors.newFixedThreadPool(App.POOL_SIZE), 10, App.NB_TRANSACTIONS,
				Transaction::new);

		logger.info("Application is closing...");
	}

}
