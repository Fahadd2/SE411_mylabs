package edu.psu.se411.lab09_inventory.model;

public class ElectronicDevice extends Item {

	private final String category;

	public ElectronicDevice(int id, String name, String category) {
		super(id, name);
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	@Override
	protected String details() {
		return "category: " + category;
	}
}
