package edu.psu.se411.lab12_threads.model;

import java.util.concurrent.locks.ReentrantLock;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * "synchronized" replaced by an explicit ReentrantLock. The lock is exposed so a
 * Transaction can hold the locks of BOTH accounts for the whole transfer.
 * It is reentrant, so the transaction can still call addToBalance /
 * retrieveFromBalance while it already holds the lock.
 */
public class CryptoAccount {
	private final String id;
	private Double balance;
	private final ReentrantLock lock = new ReentrantLock();

	public CryptoAccount(String id, Double balance) {
		if(balance < 0) throw new IllegalArgumentException("Account balance cannot be negative");
		this.id = id;
		this.balance = balance;
	}

	public void addToBalance(Double amount) {
		if(amount < 0) throw new IllegalArgumentException("Amount added to balance cannot be negative");
		lock.lock();
		try {
			balance += amount;
		} finally {
			lock.unlock();
		}
	}

	public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		lock.lock();
		try {
			if(amount > balance) throw new InsufficientBalanceException(id);
			balance -= amount;
		} finally {
			lock.unlock();
		}
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public String getId() {
		return id;
	}

	public Double getBalance() {
		lock.lock();
		try {
			return balance;
		} finally {
			lock.unlock();
		}
	}

}
