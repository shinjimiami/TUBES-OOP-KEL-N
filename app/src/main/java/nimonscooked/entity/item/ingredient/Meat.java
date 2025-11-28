package nimonscooked.entity.item.ingredient;

public class Meat extends Ingredient{
    public Meat(String id, float x, float y, IngredientState initialState) {
        super(id, "Meat", x, y, initialState);
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