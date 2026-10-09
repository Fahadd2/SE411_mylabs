package edu.psu.se411.lab09_inventory.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.psu.se411.lab09_inventory.search.SearchStrategy;

/** A type-safe inventory that holds objects of type T. */
public class Inventory<T> {

	private final List<T> items = new ArrayList<>();

	public void addItem(T item) {
		if (item == null) {
			throw new IllegalArgumentException("Cannot add a null item to the inventory");
		}
		items.add(item);
	}

	/** Returns true if the item was in the inventory. */
	public boolean removeItem(T item) {
		return items.remove(item);
	}

	/** Read-only view, so callers cannot bypass addItem/removeItem. */
	public List<T> getAllItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * Returns the items that match the strategy. "? super T" lets a strategy
	 * written for a parent type (e.g. SearchByName for any Item) also search an
	 * Inventory&lt;Book&gt;.
	 */
	public List<T> findItems(SearchStrategy<? super T> strategy) {
		List<T> result = new ArrayList<>();
		for (T item : items) {
			if (strategy.matches(item)) {
				result.add(item);
			}
		}
		return result;
	}

	public int size() {
		return items.size();
	}
}
