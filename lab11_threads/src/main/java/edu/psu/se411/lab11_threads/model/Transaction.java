package edu.psu.se411.lab11_threads.model;

import java.util.UUID;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class Transaction {
	// When false, transactions run without any locking (Part 1: demonstrates double spending).
	// When true, transactions go through the synchronized method (Part 2: the fix).
	public static boolean synchronizedMode = false;

	private String id;
	private CryptoAccount sender;
	private CryptoAccount receiver;
	private Double amount;
	
	public Transaction(CryptoAccount sender, CryptoAccount receiver, Double amount) {
		this(UUID.randomUUID().toString(), sender, receiver, amount);
	}
	
	public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
		super();
		if(id == null || sender == null || receiver == null || amount < 0) throw new IllegalArgumentException();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}
	
	public void execute() throws InsufficientBalanceException {
		// Sending money to yourself changes nothing, so skip it
		// (the random selection in App can pick the same account twice)
		if (sender == receiver) {
			return;
		}
		if (synchronizedMode) {
			transferSynchronized(sender, receiver, amount);
		} else {
			transfer(sender, receiver, amount);
		}
	}
	
	private static void transfer(CryptoAccount sender, CryptoAccount receiver, Double amount) throws InsufficientBalanceException {
		sender.retrieveFromBalance(amount);
		receiver.addToBalance(amount);
	}
	
	// static synchronized locks on Transaction.class, so only one transfer runs at a time
	// across ALL Transaction objects. (A non-static synchronized execute() would not work:
	// each thread has its own Transaction instance, hence its own lock.)
	private static synchronized void transferSynchronized(CryptoAccount sender, CryptoAccount receiver, Double amount) throws InsufficientBalanceException {
		transfer(sender, receiver, amount);
	}
	
	public CryptoAccount getSender() {
		return sender;
	}
	public void setSender(CryptoAccount sender) {
		this.sender = sender;
	}
	public CryptoAccount getReceiver() {
		return receiver;
	}
	public void setReceiver(CryptoAccount receiver) {
		this.receiver = receiver;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getId() {
		return id;
	}
}
