package nimonscooked.interfaces;

import nimonscooked.enums.IngredientState;

public interface Preparable {
    boolean canBeChopped();
    boolean canBeCooked();
    boolean canBePlacedOnPlate();

    void chop();
    void cook();

    IngredientState getState();
    String getName();
}
