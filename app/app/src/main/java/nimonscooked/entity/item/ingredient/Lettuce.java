package nimonscooked.entity.item.ingredient;

import nimonscooked.enums.IngredientState;

public class Lettuce extends Ingredient{
    public Lettuce(String id, float x, float y, IngredientState currentState) {
        super(id, "Lettuce", x, y, currentState);
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