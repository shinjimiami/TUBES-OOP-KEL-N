package nimonscooked.entity.item.ingredient;

public abstract class Ingredient extends Item implements Preparable{
    protected IngredientState currentState;
    // protected IngredientState finalState;

    public Ingredient(String id, String name, float x, float y, IngredientState initialState) {
        super(id, name, x, y);
        this.currentState = initialState;
    }

    public void requestCut(){
        if (currentState.canBeChopped()){
            currentState.chop(this);
        }
    }

    public void requestCook(){
        if (currentState.canBeCooked()){
            currentState.cook(this);
        }
    }

    public IngredientState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(IngredientState state) {
        this.currentState = state;
    }

    // berfungsi supaya nanti tinggal direplace
    @Override
    public boolean canBeChopped() {
        return false;
    }

    // berfungsi supaya nanti tinggal direplace
    @Override
    public boolean canBeCooked() {
        return false;
    }

    // berfungsi supaya nanti tinggal direplace
    @Override
    public boolean canBePlacedOnPlate() {
        return false;
    }

    @Override
    public void chop() {
        if (canBeChopped()) {
            currentState = IngredientState.CHOPPED;
        }
    }

    @Override
    public void cook() {
        if (canBeCooked()) {
            currentState = IngredientState.COOKED;
        }
    }
}