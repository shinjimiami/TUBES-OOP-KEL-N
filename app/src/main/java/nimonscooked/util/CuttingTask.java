package nimonscooked.util;

import nimonscooked.entity.Chef;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;

/**
 * CONCURRENCY: CuttingTask runs on separate thread (Multithreading)
 * Chef is BUSY during cutting operation (3 seconds)
 */
public class CuttingTask implements Runnable {
    private final Chef chef;
    private final Preparable item;
    private final int duration;
    private final Runnable onComplete;

    public CuttingTask(Chef chef, Preparable item, int durationMs, Runnable onComplete) {
        this.chef = chef;
        this.item = item;
        this.duration = durationMs;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        try {
            // Set chef to BUSY
            chef.setCurrentAction(nimonscooked.enums.ChefStatus.BUSY);
            System.out.println("[CUTTING THREAD] Started cutting " + item.getName() +
                    " - Chef is BUSY for " + (duration / 1000) + " seconds");

            // Simulate cutting duration
            Thread.sleep(duration);

            // Check if item is still RAW before chopping
            if (item.getState() == IngredientState.RAW) {
                item.chop();
                System.out.println("[CUTTING THREAD] " + item.getName() + " has been CHOPPED!");
            }

            // Release chef from BUSY status
            chef.setCurrentAction(nimonscooked.enums.ChefStatus.IDLE);
            System.out.println("[CUTTING THREAD] Chef is now IDLE");

            // Execute completion callback
            if (onComplete != null) {
                onComplete.run();
            }

        } catch (InterruptedException e) {
            System.err.println("[CUTTING THREAD] Cutting interrupted: " + e.getMessage());
            chef.setCurrentAction(nimonscooked.enums.ChefStatus.IDLE);
            Thread.currentThread().interrupt();
        }
    }
}
