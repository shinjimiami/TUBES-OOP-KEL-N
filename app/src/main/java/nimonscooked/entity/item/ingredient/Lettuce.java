package nimonscooked.entity.item.ingredient;

public class Lettuce extends Ingredient{
    public Lettuce(String id, float x, float y, IngredientState initialState) {
        super(id, "Lettuce", x, y, initialState);
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