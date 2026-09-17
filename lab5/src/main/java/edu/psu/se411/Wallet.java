package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;

public class Wallet {

    private final String owner;
    private double balance;

    public Wallet(String owner, double openingBalance) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("owner must not be null or blank");
        }
        if (Double.isNaN(openingBalance) || Double.isInfinite(openingBalance) || openingBalance < 0) {
            throw new IllegalArgumentException("openingBalance must be a non-negative finite value");
        }

        this.owner = owner;
        this.balance = openingBalance;
    }

    public void withdrawToBank(double amount, BankAccount destination) throws InsufficientFundsException {
        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite value");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Cannot withdraw " + amount + " from wallet: only " + balance + " available.");
        }

        // Perform deposit first; only adjust wallet balance after successful deposit to avoid
        // leaving this Wallet debited if destination.deposit unexpectedly throws.
        try {
            destination.deposit(amount);
            balance -= amount;
            System.out.println("Withdrew " + amount + " from " + owner + "'s wallet to "
                + destination.getOwner() + "'s bank account.");
        } catch (RuntimeException e) {
            // Wrap unexpected runtime exceptions to preserve the cause and signal failure.
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}
