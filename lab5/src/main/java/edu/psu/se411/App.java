package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;
import edu.psu.se411.exceptions.InvalidAgeException;

public class App {

    public static void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Invalid age: " + age + " (must be 18 or higher).");
        }
        System.out.println("Age valid message.");
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Custom Exceptions ===");

        try {
            validateAge(25);
        } catch (InvalidAgeException e) {
            System.out.println("Caught InvalidAgeException: " + e.getMessage());
        }

        try {
            validateAge(15);
        } catch (InvalidAgeException e) {
            System.out.println("Caught InvalidAgeException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Exercise 2: Online Wallet ===");

        Wallet wallet = new Wallet("Fahad", 100.0);
        BankAccount bank = new BankAccount("Fahad", 500.0);

        System.out.println("Starting wallet balance: " + wallet.getBalance());
        System.out.println("Starting bank balance:   " + bank.getBalance());

        try {
            wallet.withdrawToBank(40.0, bank);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
        }

        try {
            wallet.withdrawToBank(500.0, bank);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
        }

        System.out.println("Final wallet balance: " + wallet.getBalance());
        System.out.println("Final bank balance:   " + bank.getBalance());
    }
}
