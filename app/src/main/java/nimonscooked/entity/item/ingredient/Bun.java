package nimonscooked.entity.item.ingredient;

public class Bun extends Ingredient {
    public Bun(String id, float x, float y, IngredientState initialState) {
        super(id, "Bun", x, y, initialState);
        // this.setImage(getItemImage("bun")); ini nanti aja tapi buat inget gambarnya
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
