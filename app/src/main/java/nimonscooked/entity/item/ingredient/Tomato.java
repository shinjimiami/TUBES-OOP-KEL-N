package nimonscooked.entity.item.ingredient;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.IngredientState;

public class Tomato extends Ingredient{
    public Tomato(GamePanel gp, IngredientState currentState) {
        super(gp, currentState, nimonscooked.enums.IngredientType.TOMATO);
        name = "Tomato";
        registerStateImages("/items/ingredients/tomato");
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