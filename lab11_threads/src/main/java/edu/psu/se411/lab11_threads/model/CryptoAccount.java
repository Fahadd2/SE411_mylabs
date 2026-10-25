package edu.psu.se411.lab11_threads.model;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {
	private String id;
	private Double balance;
	
	public CryptoAccount(String id, Double balance) {
		this.id = id;
		this.balance = balance;
	}
	
	public void addToBalance(Double amount) {
		// Read-modify-write: not atomic, so concurrent calls can lose updates
		Double current = balance;
		Thread.yield(); // widen the race window to make the problem visible
		balance = current + amount;
	}
	
	public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		// Check-then-act + read-modify-write: not atomic either
		Double current = balance;
		if (current < amount) {
			throw new InsufficientBalanceException("Account " + id + " has " + current + ", cannot retrieve " + amount);
		}
		Thread.yield(); // widen the race window to make the problem visible
		balance = current - amount;
	}
	
	public String getId() {
		return id;
	}
	public Double getBalance() {
		return balance;
	}
	
}
