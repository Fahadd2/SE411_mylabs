package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Item;

/** Exact name match, ignoring upper/lower case. */
public class SearchByName implements SearchStrategy<Item> {

	private final String name;

	public SearchByName(String name) {
		this.name = name;
	}

	@Override
	public boolean matches(Item item) {
		return item.getName().equalsIgnoreCase(name);
	}
}
