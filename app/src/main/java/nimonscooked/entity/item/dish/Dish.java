package nimonscooked.entity.item.dish;

import java.util.ArrayList;
import java.util.List;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.Item;
import nimonscooked.enums.IngredientState;
import nimonscooked.interfaces.Preparable;

public class Dish extends Item {
    private final List<Preparable> components;

    public Dish(GamePanel gp) {
        super(gp);
        this.components = new ArrayList<>();
    }

    public boolean addComponent(Preparable component) {
        if (component == null) {
            return false;
        }

        // BURNED ingredients are always rejected
        if (component.getState() == IngredientState.BURNED) {
            return false;
        }

        // RAW ingredients are only allowed if they can't be chopped or cooked (e.g.,
        // Bun)
        if (component.getState() == IngredientState.RAW) {
            if (component.canBeChopped() || component.canBeCooked()) {
                return false; // RAW ingredients that SHOULD be processed are rejected
            }
            // RAW ingredients that DON'T need processing (like Bun) are allowed
        }

        // COOKING ingredients are rejected (must wait until COOKED)
        if (component.getState() == IngredientState.COOKING) {
            System.out.println("[DISH] Rejected: " + component.getName() + " is still COOKING (not COOKED yet)");
            return false;
        }

        // CHOPPED ingredients are allowed (cheese, lettuce, tomato)
        // COOKED ingredients are allowed (meat after cooking)

        // Prevent duplicate ingredients
        for (Preparable existingComponent : components) {
            if (existingComponent.getName().equals(component.getName())) {
                return false;
            }
        }

        return components.add(component);
    }

    public List<Preparable> getComponents() {
        return new ArrayList<>(components);
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public void clearDish() {
        components.clear();
    }

}
