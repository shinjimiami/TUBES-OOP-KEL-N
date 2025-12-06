package nimonscooked.entity.item.ingredient;

import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.Item;
import nimonscooked.enums.IngredientState;

public abstract class Ingredient extends Item implements Preparable{
    protected IngredientState currentState;
    // protected IngredientState finalState;

    public Ingredient(String id, String name, float x, float y, IngredientState initialState) {
        super(id, name, x, y);
        this.currentState = initialState;
    }

    public IngredientState getState() {
        return currentState;
    }

    public void setCurrentState(IngredientState state) {
        this.currentState = state;
    }

    // berfungsi supaya nanti tinggal direplace
    @Override
    public abstract boolean canBeChopped();

    // berfungsi supaya nanti tinggal direplace
    @Override
    public abstract boolean canBeCooked();

    // berfungsi supaya nanti tinggal direplace
    @Override
    public abstract boolean canBePlacedOnPlate();

    @Override
    public void chop() {
        if (canBeChopped()) {
            currentState = IngredientState.CHOPPED;
        }
    }

    @Override
    public void cook() {
        if (canBeCooked()) {
            if(currentState == IngredientState.CHOPPED){
                currentState = IngredientState.COOKED;
            } else if(currentState == IngredientState.COOKED){
                currentState = IngredientState.BURNED;
            }
        }
    }
}