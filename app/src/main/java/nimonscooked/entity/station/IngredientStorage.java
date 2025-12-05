package nimonscooked.entity.station;

import nimonscooked.entity.Chef;

// berfungsi untuk menyimpan ingredient yang diambil pemain
// ingredient ini bersifat unlimited
public class IngredientStorage extends Station {

    public IngredientStorage(String id, int x, int y) {
        super(id, "Ingredient Storage", x, y);
    }
    
	@Override
	public void interact(Chef player) {
		// placeholder
	}    
}
