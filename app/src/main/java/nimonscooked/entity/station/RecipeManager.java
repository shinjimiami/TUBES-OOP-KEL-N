package nimonscooked.entity.station;

import nimonscooked.entity.item.dish.Dish;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeManager {

    public static String getRecipeName(Dish dish) {
        if (dish == null || dish.getComponents().isEmpty())
            return null;

        List<Preparable> components = dish.getComponents();
        Set<String> compNames = components.stream()
                .map(Preparable::getName)
                .collect(Collectors.toSet());

        boolean hasBun = compNames.contains("Bun");
        boolean hasCookedMeat = hasIngredientState(components, "Meat", IngredientState.COOKED);
        boolean hasChoppedCheese = hasIngredientState(components, "Cheese", IngredientState.CHOPPED);
        boolean hasChoppedLettuce = hasIngredientState(components, "Lettuce", IngredientState.CHOPPED);
        boolean hasChoppedTomato = hasIngredientState(components, "Tomato", IngredientState.CHOPPED);

        int size = components.size();

        // 1. Classic Burger: Roti + Daging Matang
        if (size == 2 && hasBun && hasCookedMeat)
            return "Classic Burger";

        // 2. Cheeseburger: Roti + Daging Matang + Keju Potong
        if (size == 3 && hasBun && hasCookedMeat && hasChoppedCheese)
            return "Cheeseburger";

        // 3. BLT Burger: Roti + Daging Matang + Lettuce Potong + Tomat Potong
        if (size == 4 && hasBun && hasCookedMeat && hasChoppedLettuce && hasChoppedTomato)
            return "BLT Burger";

        // 4. Deluxe Burger: Roti + Daging Matang + Lettuce Potong + Keju Potong
        if (size == 4 && hasBun && hasCookedMeat && hasChoppedLettuce && hasChoppedCheese)
            return "Deluxe Burger";

        return null;
    }

    private static boolean hasIngredientState(List<Preparable> list, String name, IngredientState state) {
        return list.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name) && p.getState() == state);
    }
}