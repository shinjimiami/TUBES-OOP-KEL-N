package nimonscooked.entity.item.ingredient;

public class Cheese extends Ingredient{
    public Cheese(String id, float x, float y, IngredientState initialState) {
        super(id, "Cheese", x, y, initialState);
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