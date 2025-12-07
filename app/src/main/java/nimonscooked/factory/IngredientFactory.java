package nimonscooked.factory;

import nimonscooked.entity.item.ingredient.*;
import nimonscooked.enums.IngredientType;
import nimonscooked.enums.IngredientState;

public class IngredientFactory {

    // DESIGN PATTERN: Factory Method
    public static Ingredient createIngredient(IngredientType type) {
        // ID digenerate random atau sequence agar unik
        String id = "ING-" + System.currentTimeMillis();

        return switch (type) {
            case BUN -> new Bun(id, 0, 0, IngredientState.RAW);
            case MEAT -> new Meat(id, 0, 0, IngredientState.RAW);
            case CHEESE -> new Cheese(id, 0, 0, IngredientState.RAW);
            case LETTUCE -> new Lettuce(id, 0, 0, IngredientState.RAW);
            case TOMATO -> new Tomato(id, 0, 0, IngredientState.RAW);
            default -> null;
        };
    }
}