package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Book;

public class SearchByAuthor implements SearchStrategy<Book> {

	private final String author;

	public SearchByAuthor(String author) {
		this.author = author;
	}

	@Override
	public boolean matches(Book book) {
		return book.getAuthor().equalsIgnoreCase(author);
	}
}
