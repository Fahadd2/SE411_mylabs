package edu.psu.se411.lab09_inventory.model;

/** Data shared by every product in the inventory. */
public abstract class Item {

	private final int id;
	private final String name;

	protected Item(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/** Type-specific details, appended to the common ones by toString(). */
	protected abstract String details();

	@Override
	public String toString() {
		return String.format("%-16s #%d  %-28s %s", getClass().getSimpleName(), id, name, details());
	}
}
