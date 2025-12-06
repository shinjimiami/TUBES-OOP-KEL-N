package nimonscooked.entity.station;

import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.dish.Dish;

public class ServingCounter extends Station {

    public ServingCounter(String id, float x, float y) {
        super(id, "Serving Counter", x, y);
    }
    
	@Override
	public void interact(ChefPlayer player) {
		// placeholder
	}    

    public void serveItem(Plate plate){
        Dish dish = plate.getDish();

        if(RecipeManager.checkRecipe(dish)){
            // masih dipikirin, intinya ngecek apakah ada order yang sama ama ini
            plate.setDirty(true);
        }

    }
}