package nimonscooked.entity.station;

// berfungsi untuk menyimpan ingredient yang diambil pemain
// ingredient ini bersifat unlimited
public class IngredientStorage extends Station {

    public IngredientStorage(String id, float x, float y) {
        super(id, "Ingredient Storage", x, y);
    }
    
	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}    
}
