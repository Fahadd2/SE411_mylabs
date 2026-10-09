package edu.psu.se411.lab09_inventory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab09_inventory.inventory.Inventory;
import edu.psu.se411.lab09_inventory.model.Book;
import edu.psu.se411.lab09_inventory.model.ElectronicDevice;
import edu.psu.se411.lab09_inventory.model.Item;
import edu.psu.se411.lab09_inventory.search.SearchByAuthor;
import edu.psu.se411.lab09_inventory.search.SearchByCategory;
import edu.psu.se411.lab09_inventory.search.SearchById;
import edu.psu.se411.lab09_inventory.search.SearchByName;
import edu.psu.se411.lab09_inventory.search.SearchOfType;
import edu.psu.se411.lab09_inventory.search.SearchStrategy;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");
		try {
			// ---------- Part 1: generic inventory ----------
			System.out.println("=== Part 1: Inventory<Item> with books and electronic devices ===");
			Inventory<Item> inventory = new Inventory<>();
			Book concurrency = new Book(1, "Concurrency in Java", "Brian Goetz");
			Book effective = new Book(2, "Effective Java", "Joshua Bloch");
			Book puzzlers = new Book(3, "Java Puzzlers", "Joshua Bloch");
			ElectronicDevice phone = new ElectronicDevice(4, "iPhone 15", "Phone");
			ElectronicDevice laptop = new ElectronicDevice(5, "ThinkPad X1", "Laptop");
			ElectronicDevice galaxy = new ElectronicDevice(6, "Galaxy S24", "Phone");
			ElectronicDevice headphones = new ElectronicDevice(7, "AirPods Pro", "Audio");

			inventory.addItem(concurrency);
			inventory.addItem(effective);
			inventory.addItem(puzzlers);
			inventory.addItem(phone);
			inventory.addItem(laptop);
			inventory.addItem(galaxy);
			inventory.addItem(headphones);
			System.out.println("Added " + inventory.size() + " items.");

			System.out.println("Removing " + headphones.getName() + ": " + inventory.removeItem(headphones));
			System.out.println("Items now: " + inventory.size());

			try {
				inventory.addItem(null);
			} catch (IllegalArgumentException e) {
				System.out.println("Exception caught: " + e.getMessage());
				logger.error("Could not add item", e);
			}

			// ---------- Part 2: strategy pattern ----------
			System.out.println();
			System.out.println("=== Part 2: searching with SearchStrategy ===");
			printResults("By name \"Concurrency in Java\"", inventory.findItems(new SearchByName("Concurrency in Java")));
			printResults("By name \"iPhone 15\"", inventory.findItems(new SearchByName("iPhone 15")));
			printResults("By id 5", inventory.findItems(new SearchById(5)));

			// Lambda as a strategy: SearchStrategy is a functional interface
			printResults("Lambda: name contains \"java\"",
					inventory.findItems(item -> item.getName().toLowerCase().contains("java")));
			SearchStrategy<Item> evenId = item -> item.getId() % 2 == 0;
			printResults("Lambda: even id", inventory.findItems(evenId));

			// Type-specific strategies on type-specific inventories
			Inventory<Book> books = new Inventory<>();
			books.addItem(concurrency);
			books.addItem(effective);
			books.addItem(puzzlers);
			Inventory<ElectronicDevice> devices = new Inventory<>();
			devices.addItem(phone);
			devices.addItem(laptop);
			devices.addItem(galaxy);

			printResults("Books by author \"Joshua Bloch\"", books.findItems(new SearchByAuthor("Joshua Bloch")));
			printResults("Devices in category \"Phone\"", devices.findItems(new SearchByCategory("Phone")));
			// An Item strategy also works on Inventory<Book> thanks to "? super T"
			printResults("Books by name \"Effective Java\"", books.findItems(new SearchByName("Effective Java")));

			// Type-specific strategies on the mixed inventory
			printResults("Mixed inventory, books by \"Joshua Bloch\"",
					inventory.findItems(new SearchOfType<>(Book.class, new SearchByAuthor("Joshua Bloch"))));
			printResults("Mixed inventory, \"Laptop\" devices",
					inventory.findItems(new SearchOfType<>(ElectronicDevice.class, new SearchByCategory("Laptop"))));

			// ---------- Part 3: display any inventory ----------
			System.out.println();
			System.out.println("=== Part 3: displayInventory(Inventory<?>) ===");
			displayInventory(inventory);
			displayInventory(books);
			displayInventory(devices);
		} finally {
			logger.info("Application is closing...");
		}
	}

	/** Prints every item of any inventory, whatever its element type. */
	public static void displayInventory(Inventory<?> inventory) {
		System.out.println("Inventory (" + inventory.size() + " items):");
		for (Object item : inventory.getAllItems()) {
			System.out.println("  " + item);
		}
	}

	private static void printResults(String title, List<? extends Item> results) {
		System.out.println(title + " -> " + results.size() + " found");
		for (Item item : results) {
			System.out.println("  " + item);
		}
	}

}
