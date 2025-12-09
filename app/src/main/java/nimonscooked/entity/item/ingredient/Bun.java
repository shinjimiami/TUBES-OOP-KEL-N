package nimonscooked.entity.item.ingredient;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.IngredientState;

public class Bun extends Ingredient {
    public Bun(GamePanel gp, IngredientState currentState) {
        super(gp, currentState, nimonscooked.enums.IngredientType.BUN);
        name = "Bun";
        registerStateImages("/items/ingredients/bun");

    }

    @Override
    public boolean canBeChopped() {
        return false;
    }

    @Override
    public boolean canBeCooked() {
        return false;
    }

    @Override
    public boolean canBePlacedOnPlate() {
        return currentState == IngredientState.RAW;
    }

}
