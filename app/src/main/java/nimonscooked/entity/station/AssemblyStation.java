package nimonscooked.entity.station;

import nimonscooked.entity.Chef;

// berfungsi seperti countertop biasa
// namun hanya untuk merakit/menggabungkan beberapa ingredient menjadi sebuah dish
public class AssemblyStation extends Station {

	public AssemblyStation(String id, int x, int y) {
		super(id, "Assembly Station", x, y);
	}

	@Override
	public void interact(Chef chef) {
		// placeholder
		if (this.containedItem == null && chef.getHeldItem() != null) {
			placeItem(chef.takeItem());
		} else if (this.containedItem != null && chef.getHeldItem() == null) {
			chef.placeItem(takeItem());
		}

	}

}