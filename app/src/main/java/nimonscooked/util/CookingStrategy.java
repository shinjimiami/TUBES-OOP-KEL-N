package nimonscooked.util;

import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;

/**
 * STRATEGY PATTERN: Concrete strategy for cooking ingredients
 */
public class CookingStrategy implements ProcessingStrategy {
    private final Preparable item;
    private final int cookingDuration;
    private final int burningDuration;
    private int elapsedTime = 0;

    public CookingStrategy(Preparable item, int cookDurationMs, int burnDurationMs) {
        this.item = item;
        this.cookingDuration = cookDurationMs;
        this.burningDuration = burnDurationMs;
    }

    @Override
    public boolean process(int durationMs) {
        elapsedTime += durationMs;

        if (elapsedTime >= burningDuration) {
            if (item.getState() == IngredientState.COOKED) {
                item.cook(); // COOKED -> BURNED
                System.out.println("[COOKING STRATEGY] " + item.getName() + " is BURNED!");
                return true;
            }
        } else if (elapsedTime >= cookingDuration) {
            if (item.getState() == IngredientState.CHOPPED ||
                    item.getState() == IngredientState.COOKING) {
                item.cook(); // -> COOKED
                System.out.println("[COOKING STRATEGY] " + item.getName() + " is COOKED!");
                return true;
            }
        }

        return false;
    }

    @Override
    public String getProcessType() {
        return "COOKING";
    }

    @Override
    public int getRequiredDuration() {
        return cookingDuration;
    }

    @Override
    public void reset() {
        elapsedTime = 0;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public float getProgress() {
        if (elapsedTime < cookingDuration) {
            return (float) elapsedTime / cookingDuration;
        } else {
            return 1.0f + ((float) (elapsedTime - cookingDuration) / (burningDuration - cookingDuration));
        }
    }
}
