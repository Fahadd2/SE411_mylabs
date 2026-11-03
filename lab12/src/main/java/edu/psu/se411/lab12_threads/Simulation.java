package edu.psu.se411.lab12_threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * The double-spending simulation shared by every main program: create the
 * accounts, run random transactions on the given thread pool, then check that
 * no money was created or lost.
 */
public final class Simulation {

	static Logger logger = LoggerFactory.getLogger(Simulation.class);

	public static final double INITIAL_BALANCE = 10000.0;

	/** Lets each program choose which kind of Transaction to run. */
	@FunctionalInterface
	public interface TransactionFactory {
		Transaction create(String id, CryptoAccount sender, CryptoAccount receiver, Double amount);
	}

	private Simulation() {
	}

	/** Runs the simulation and returns its duration in milliseconds, or -1 if it deadlocked. */
	public static long run(String title, ExecutorService executor, int nbAccounts, int nbTransactions,
			TransactionFactory factory) {
		System.out.printf("--- %s: %,d accounts, %,d transactions ---%n", title, nbAccounts, nbTransactions);
		logger.info("{}: {} accounts, {} transactions", title, nbAccounts, nbTransactions);

		// 1- Create accounts
		CryptoAccount[] accounts = new CryptoAccount[nbAccounts];
		for (int i = 0; i < nbAccounts; i++) {
			accounts[i] = new CryptoAccount("acc_" + i, INITIAL_BALANCE);
		}
		double initialTotal = sumTotalMoneyInAccounts(accounts);

		ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		int threadsBefore = threads.getThreadCount();
		threads.resetPeakThreadCount();
		long startTime = System.currentTimeMillis();
		ThreadLocalRandom random = ThreadLocalRandom.current();

		// 2- choose sender and receiver accounts randomly, 3- execute transaction
		for (int i = 0; i < nbTransactions; i++) {
			int senderIndex = random.nextInt(nbAccounts);
			int receiverIndex = random.nextInt(nbAccounts);
			if (senderIndex == receiverIndex) {
				receiverIndex = (receiverIndex + 1) % nbAccounts;
			}
			double amount = random.nextDouble() * 100;
			executor.execute(factory.create("id_" + i, accounts[senderIndex], accounts[receiverIndex], amount));
		}

		// 4- wait for all transactions to complete (or detect a deadlock)
		executor.shutdown();
		boolean finished = awaitOrDetectDeadlock(executor);
		long elapsed = System.currentTimeMillis() - startTime;

		// 5- check total money in system after all transactions
		double finalTotal = sumTotalMoneyInAccounts(accounts);
		System.out.printf("Money before: %,.2f   after: %,.2f   %s%n", initialTotal, finalTotal,
				Math.abs(initialTotal - finalTotal) < 0.01 ? "(conserved)" : "(MONEY LOST OR CREATED!)");
		System.out.printf("Peak pool threads: %,d%n", Math.max(0, threads.getPeakThreadCount() - threadsBefore));
		if (finished) {
			System.out.printf("Total time: %,d ms%n%n", elapsed);
		} else {
			System.out.printf("Stopped after %,d ms because of the deadlock%n%n", elapsed);
		}
		return finished ? elapsed : -1;
	}

	/**
	 * Waits for the pool to finish. Every 2 seconds it asks the JVM whether some
	 * threads are deadlocked; if so it prints them and cancels the pool.
	 */
	private static boolean awaitOrDetectDeadlock(ExecutorService executor) {
		ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		try {
			while (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
				long[] deadlocked = threads.findDeadlockedThreads();
				if (deadlocked != null) {
					System.out.printf("DEADLOCK detected between %d threads:%n", deadlocked.length);
					for (ThreadInfo info : threads.getThreadInfo(deadlocked)) {
						System.out.printf("  %s waits for %s held by %s%n", info.getThreadName(),
								info.getLockName(), info.getLockOwnerName());
					}
					logger.error("Deadlock detected between {} threads", deadlocked.length);
					executor.shutdownNow(); // interrupts the stuck threads (they use lockInterruptibly)
					executor.awaitTermination(10, TimeUnit.SECONDS);
					return false;
				}
			}
			return true;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	private static double sumTotalMoneyInAccounts(CryptoAccount[] accounts) {
		double total = 0.0;
		for (CryptoAccount c : accounts) {
			total += c.getBalance();
		}
		return total;
	}
}
