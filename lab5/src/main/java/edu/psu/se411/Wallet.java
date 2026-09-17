package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;

public class Wallet {

    private final String owner;
    private double balance;

    public Wallet(String owner, double openingBalance) {
        this.owner = owner;
        this.balance = openingBalance;
    }

    public void withdrawToBank(double amount, BankAccount destination) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Cannot withdraw " + amount + " from wallet: only " + balance + " available.");
        }
        balance -= amount;
        destination.deposit(amount);
        System.out.println("Withdrew " + amount + " from " + owner + "'s wallet to "
            + destination.getOwner() + "'s bank account.");
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}
