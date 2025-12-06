package nimonscooked.entity.item.ingredient;

import nimonscooked.enums.IngredientState;

public class Meat extends Ingredient{
    public Meat(String id, float x, float y, IngredientState currentState) {
        super(id, "Meat", x, y, currentState);
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