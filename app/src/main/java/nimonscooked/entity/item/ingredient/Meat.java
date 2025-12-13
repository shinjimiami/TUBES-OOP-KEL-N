package nimonscooked.entity.item.ingredient;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.IngredientState;

public class Meat extends Ingredient{
    public Meat(GamePanel gp, IngredientState currentState) {
        super(gp, currentState, nimonscooked.enums.IngredientType.MEAT);
        name = "Meat";
        registerStateImages("/items/ingredients/meat");
    }

    @Override
    public boolean canBeChopped(){
        return currentState == IngredientState.RAW;
    }

    @Override
    public boolean canBeCooked(){
        return currentState == IngredientState.CHOPPED;
    }

    @Override
    public boolean canBePlacedOnPlate(){
        return currentState == IngredientState.COOKED;
    }
}