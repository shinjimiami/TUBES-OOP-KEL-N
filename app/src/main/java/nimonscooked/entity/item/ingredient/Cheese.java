package nimonscooked.entity.item.ingredient;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.IngredientState;

public class Cheese extends Ingredient{
    public Cheese(GamePanel gp, IngredientState currentState) {
        super(gp, currentState, nimonscooked.enums.IngredientType.CHEESE);
        name = "Cheese";
        registerStateImages("/items/ingredients/cheese");
    }

    @Override
    public boolean canBeChopped(){
        return currentState == IngredientState.RAW;
    }

    @Override
    public boolean canBeCooked(){
        return false;
    }

    @Override
    public boolean canBePlacedOnPlate(){
        return currentState == IngredientState.CHOPPED;
    }

}