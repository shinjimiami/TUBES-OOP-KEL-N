package nimonscooked.entity.item.ingredient;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.IngredientState;

public class Lettuce extends Ingredient{
    public Lettuce(GamePanel gp, IngredientState currentState) {
        super(gp, currentState, nimonscooked.enums.IngredientType.LETTUCE);
        name = "Lettuce";
        registerStateImages("/items/ingredients/lettuce");
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