package edu.psu.se411;

public class BankAccount {

    private final String owner;
    private double balance;

    public BankAccount(String owner, double openingBalance) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("owner must not be null or blank");
        }
        if (Double.isNaN(openingBalance) || Double.isInfinite(openingBalance) || openingBalance < 0) {
            throw new IllegalArgumentException("openingBalance must be a non-negative finite value");
        }

        this.owner = owner;
        this.balance = openingBalance;
    }

    /**
     * Deposit a positive amount into this account.
     * @throws IllegalArgumentException if amount is not a positive finite value
     */
    public void deposit(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite value");
        }
        this.balance += amount;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}
