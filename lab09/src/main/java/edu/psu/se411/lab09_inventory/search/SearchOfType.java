package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.Item;

/**
 * Lets a strategy written for one item type (e.g. SearchByAuthor for Book)
 * search a mixed Inventory&lt;Item&gt;: items of other types simply don't match.
 */
public class SearchOfType<S extends Item> implements SearchStrategy<Item> {

	private final Class<S> type;
	private final SearchStrategy<? super S> strategy;

	public SearchOfType(Class<S> type, SearchStrategy<? super S> strategy) {
		this.type = type;
		this.strategy = strategy;
	}

	@Override
	public boolean matches(Item item) {
		return type.isInstance(item) && strategy.matches(type.cast(item));
	}
}
