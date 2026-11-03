package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * Transfers money while holding the locks of both accounts, so the balance
 * check and the transfer happen as one atomic step.
 *
 * Resource ordering: the two locks are always taken in ascending account id
 * order. Every thread therefore asks for the same locks in the same order, so
 * no thread can hold one lock while waiting for a lock held by a thread that
 * is waiting for it (no circular wait = no deadlock).
 */
public class Transaction implements Runnable {
	private final String id;
	private final CryptoAccount sender;
	private final CryptoAccount receiver;
	private final Double amount;

	public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
		if(id == null || sender == null || receiver == null || amount == null) throw new IllegalArgumentException();
		if(amount < 0) throw new IllegalArgumentException("Amount used in transaction cannot be negative");
		if(sender == receiver) throw new IllegalArgumentException("Cannot make a transaction where the sender and the receiver are the same account");
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (InsufficientBalanceException e) {
			// transaction refused, nothing was moved
		} catch (InterruptedException e) {
			// cancelled while waiting for a lock (e.g. deadlock recovery), nothing was moved
			Thread.currentThread().interrupt();
		}
	}

	public void execute() throws InsufficientBalanceException, InterruptedException {
		CryptoAccount first = firstToLock();
		CryptoAccount second = (first == sender) ? receiver : sender;

		// lockInterruptibly: a thread stuck waiting can still be cancelled
		first.getLock().lockInterruptibly();
		try {
			second.getLock().lockInterruptibly();
			try {
				sender.retrieveFromBalance(amount);
				receiver.addToBalance(amount);
			} finally {
				second.getLock().unlock();
			}
		} finally {
			first.getLock().unlock();
		}
	}

	/** Total order on accounts: the smaller id is always locked first. */
	protected CryptoAccount firstToLock() {
		return sender.getId().compareTo(receiver.getId()) < 0 ? sender : receiver;
	}

	public CryptoAccount getSender() {
		return sender;
	}
	public CryptoAccount getReceiver() {
		return receiver;
	}
	public Double getAmount() {
		return amount;
	}
	public String getId() {
		return id;
	}

}
