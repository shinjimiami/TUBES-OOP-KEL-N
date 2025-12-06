package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.ingredient.Bun;
import nimonscooked.entity.item.ingredient.Cheese;
import nimonscooked.entity.item.ingredient.Lettuce;
import nimonscooked.entity.item.ingredient.Meat;
import nimonscooked.entity.item.ingredient.Tomato;
import nimonscooked.enums.IngredientState;

// berfungsi untuk menyimpan ingredient yang diambil pemain
// ingredient ini bersifat unlimited
public class IngredientStorage extends Station {

    public IngredientStorage(String id, float x, float y) {
        super(id, "Ingredient Storage", x, y);
    }

    @Override
    public void interact(Chef chef) {
        // Only spawn new ingredient if the chef's hands are free
        if (chef.getHeldItem() != null) {
            return;
        }

        Item newIngredient = spawnIngredient();
        if (newIngredient != null) {
            chef.setHeldItem(newIngredient);
        }
    }

    private Item spawnIngredient() {
        String key = getId().toLowerCase();
        float x = getX();
        float y = getY();

        if (key.contains("bun")) {
            return new Bun("BUN_" + System.nanoTime(), x, y, IngredientState.RAW);
        }
        if (key.contains("cheese")) {
            return new Cheese("CHEESE_" + System.nanoTime(), x, y, IngredientState.RAW);
        }
        if (key.contains("lettuce")) {
            return new Lettuce("LETTUCE_" + System.nanoTime(), x, y, IngredientState.RAW);
        }
        if (key.contains("tomato")) {
            return new Tomato("TOMATO_" + System.nanoTime(), x, y, IngredientState.RAW);
        }

        // Default ingredient
        return new Meat("MEAT_" + System.nanoTime(), x, y, IngredientState.RAW);
    }
}
