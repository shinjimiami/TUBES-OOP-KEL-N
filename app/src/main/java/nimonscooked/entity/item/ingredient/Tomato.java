package nimonscooked.entity.item.ingredient;

import nimonscooked.enums.IngredientState;

public class Tomato extends Ingredient{
    public Tomato(String id, float x, float y, IngredientState currentState) {
        super(id, "Tomato", x, y, currentState);
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