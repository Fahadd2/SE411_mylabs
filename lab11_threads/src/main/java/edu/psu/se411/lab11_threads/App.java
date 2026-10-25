package edu.psu.se411.lab11_threads;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;
import edu.psu.se411.lab11_threads.model.CryptoAccount;
import edu.psu.se411.lab11_threads.model.Transaction;

/**
 * Build and run using Maven goal: clean package exec:java
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static Integer nbAccounts = 10;
	public static Double initialBalance = 1000.0;
	public static Integer nbTransactions = 10000;

	public static void main(String[] args) {
		logger.info("Application is starting...");
		
		// Part 1: no synchronization -> balances become inconsistent
		runSimulation(false);
		
		// Part 2: synchronized transfer -> total is preserved
		runSimulation(true);

		logger.info("Application is closing...");
	}
	
	private static void runSimulation(boolean synchronizedMode) {
		Transaction.synchronizedMode = synchronizedMode;
		String mode = synchronizedMode ? "WITH synchronization" : "WITHOUT synchronization";
		
		// 1- Create accounts
		List<CryptoAccount> accounts = new ArrayList<>();
		for (Integer i = 0; i < nbAccounts; i++) {
			accounts.add(new CryptoAccount("acc_" + i.toString(), initialBalance));
		}
		System.out.println("=== Simulation " + mode + " ===");
		System.out.println("Initial total: " + totalBalance(accounts));
		
		// 2- Create threads making random transactions
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < nbTransactions; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					CryptoAccount randomA = accounts.get((int)(Math.random() * 10));
					CryptoAccount randomB = accounts.get((int)(Math.random() * 10));
					Double randomAmount = (double)(Math.random() * 10 + 1);
					Transaction t = new Transaction(randomA, randomB, randomAmount);
					try {
						t.execute();
					} catch (InsufficientBalanceException e) {
						logger.error("Transaction " + t.getId() + " rejected: " + e.getMessage());
					}
				}
			});
			threads.add(thread);
			thread.start();
		}
		
		// 3- Wait for all transactions to terminate
		for (int i = 0; i < threads.size(); i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting for transactions", e);
				Thread.currentThread().interrupt();
			}
		}
		
		// 4- Check the total amount of money in the system
		Double expected = nbAccounts * initialBalance;
		Double total = totalBalance(accounts);
		System.out.printf("Final total:   %.4f (expected %.4f)%n", total, expected);
		if (Math.abs(total - expected) > 1e-6) {
			System.out.println("INCONSISTENT: money was created or destroyed!");
			logger.warn(mode + ": inconsistent total " + total + " (expected " + expected + ")");
		} else {
			System.out.println("Consistent.");
			logger.info(mode + ": consistent total " + total);
		}
		System.out.println();
	}
	
	public static Double totalBalance(List<CryptoAccount> accounts) {
		Double total = 0.0;
		for (int i = 0; i < accounts.size(); i++) {
			total += accounts.get(i).getBalance();
		}
		return total;
	}

}
