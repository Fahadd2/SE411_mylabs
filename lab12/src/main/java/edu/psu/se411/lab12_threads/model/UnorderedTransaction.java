package edu.psu.se411.lab12_threads.model;

/**
 * What could go wrong: locks taken in "sender then receiver" order.
 *
 * Thread 1 runs A -> B: locks A, then waits for B.
 * Thread 2 runs B -> A: locks B, then waits for A.
 * Each waits forever for the other: a deadlock. Only used by DeadlockDemo.
 */
public class UnorderedTransaction extends Transaction {

	public UnorderedTransaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
		super(id, sender, receiver, amount);
	}

	@Override
	protected CryptoAccount firstToLock() {
		return getSender();
	}
}
