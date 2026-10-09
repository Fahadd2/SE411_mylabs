package edu.psu.se411.lab09_inventory.search;

/** Strategy: decides whether an item matches a search criteria. */
@FunctionalInterface
public interface SearchStrategy<T> {
    boolean matches(T item);
}
