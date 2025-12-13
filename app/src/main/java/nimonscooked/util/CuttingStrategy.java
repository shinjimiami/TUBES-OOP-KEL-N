package nimonscooked.util;

import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;

/**
 * STRATEGY PATTERN: Concrete strategy for cutting ingredients
 */
public class CuttingStrategy implements ProcessingStrategy {
    private final Preparable item;
    private final int cuttingDuration;
    private int elapsedTime = 0;

    public CuttingStrategy(Preparable item, int durationMs) {
        this.item = item;
        this.cuttingDuration = durationMs;
    }

    @Override
    public boolean process(int durationMs) {
        if (item.getState() != IngredientState.RAW) {
            return false;
        }

        elapsedTime += durationMs;

        if (elapsedTime >= cuttingDuration) {
            item.chop();
            System.out.println("[CUTTING STRATEGY] " + item.getName() + " has been chopped!");
            return true;
        }

        return false;
    }

    @Override
    public String getProcessType() {
        return "CUTTING";
    }

    @Override
    public int getRequiredDuration() {
        return cuttingDuration;
    }

    @Override
    public void reset() {
        elapsedTime = 0;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public float getProgress() {
        return (float) elapsedTime / cuttingDuration;
    }
}
