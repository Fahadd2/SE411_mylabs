package edu.spu.se411.lab06_logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab06_logging.exceptions.InsufficientFundsException;
import edu.spu.se411.lab06_logging.model.WalletAccount;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");
		try {
			WalletAccount account = new WalletAccount(1000);

			account.deposit(250);

	        try {
	            account.withdraw(1500);
	        } catch (InsufficientFundsException e) {
	            System.out.println("Exception caught: " + e.getMessage());
	            logger.error("Withdraw failed", e);
	        }

	        try {
	            account.deposit(-100);
	        } catch (IllegalArgumentException e) {
	            System.out.println("Exception caught: " + e.getMessage());
	            logger.error("Deposit failed", e);
	        }

	        try {
	            account.withdraw(200);
	        } catch (InsufficientFundsException e) {
	            System.out.println("Exception caught: " + e.getMessage());
	            logger.error("Withdraw failed", e);
	        }
		} finally {
			logger.info("Application ends.");
		}
	}

}
