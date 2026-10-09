package edu.psu.se411.lab09_inventory.model;

public class Book extends Item {

	private final String author;

	public Book(int id, String name, String author) {
		super(id, name);
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	@Override
	protected String details() {
		return "author: " + author;
	}
}
