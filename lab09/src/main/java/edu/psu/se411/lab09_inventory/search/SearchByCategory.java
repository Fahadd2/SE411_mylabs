package edu.psu.se411.lab09_inventory.search;

import edu.psu.se411.lab09_inventory.model.ElectronicDevice;

public class SearchByCategory implements SearchStrategy<ElectronicDevice> {

	private final String category;

	public SearchByCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean matches(ElectronicDevice device) {
		return device.getCategory().equalsIgnoreCase(category);
	}
}
