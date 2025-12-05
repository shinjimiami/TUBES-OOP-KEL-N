package nimonscooked.entity.item.ingredient;

import nimonscooked.enums.IngredientState;

public class Cheese extends Ingredient{
    public Cheese(String id, float x, float y, IngredientState currentState) {
        super(id, "Cheese", x, y, currentState);
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