package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.ingredient.Ingredient;
import nimonscooked.enums.IngredientType;
import nimonscooked.factory.IngredientFactory;

public class IngredientStorage extends Station {

	private IngredientType type; // Jenis bahan yang disimpan storage ini

	public IngredientStorage(String id, float x, float y, IngredientType type) {
		super(id, "Crate: " + type.name(), (int) x, (int) y);
		this.type = type;
	}

	@Override
	public void interact(Chef player) {
		// Storage ini infinite source, jadi selalu bisa ambil
		// Syarat: Tangan pemain harus kosong
		if (player.getHeldItem() == null) {
			// Minta Factory buatkan bahan baru
			Ingredient newIngredient = IngredientFactory.createIngredient(this.type);

			// Berikan ke pemain
			player.placeItem(newIngredient);
			System.out.println("Mengambil " + newIngredient.getName() + " dari Storage.");
		}
	}

	public IngredientType getType() {
		return type;
	}
}