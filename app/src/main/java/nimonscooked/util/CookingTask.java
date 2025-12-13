package nimonscooked.util;

import nimonscooked.interfaces.Preparable;
import nimonscooked.interfaces.CookingDevice;
import nimonscooked.enums.IngredientState;

/**
 * CONCURRENCY: CookingTask runs on separate thread (Multithreading)
 * Chef is FREE during cooking (automatic background process)
 */
public class CookingTask implements Runnable {
    private final CookingDevice device;
    private final int cookDuration;
    private final int burnDuration;
    private final Runnable onCooked;
    private final Runnable onBurned;
    private volatile boolean stopped = false;

    public CookingTask(CookingDevice device, int cookDurationMs, int burnDurationMs,
            Runnable onCooked, Runnable onBurned) {
        this.device = device;
        this.cookDuration = cookDurationMs;
        this.burnDuration = burnDurationMs;
        this.onCooked = onCooked;
        this.onBurned = onBurned;
    }

    @Override
    public void run() {
        try {
            Preparable item = device.getFirstIngredient();
            if (item == null) {
                System.out.println("[COOKING THREAD] No ingredient to cook");
                return;
            }

            System.out.println("[COOKING THREAD] Started cooking " + item.getName() +
                    " (Cook: " + (cookDuration / 1000) + "s, Burn: " + (burnDuration / 1000) + "s)");

            // Phase 1: COOKING -> COOKED
            long startTime = System.currentTimeMillis();
            while (!stopped) {
                long elapsed = System.currentTimeMillis() - startTime;

                if (elapsed >= burnDuration) {
                    // BURNED
                    if (item.getState() == IngredientState.COOKED) {
                        item.cook(); // COOKED -> BURNED
                        System.out.println("[COOKING THREAD] " + item.getName() + " is BURNED!");
                        if (onBurned != null)
                            onBurned.run();
                    }
                    break;
                } else if (elapsed >= cookDuration) {
                    // COOKED
                    if (item.getState() != IngredientState.COOKED &&
                            item.getState() != IngredientState.BURNED) {
                        item.cook(); // -> COOKED
                        System.out.println("[COOKING THREAD] " + item.getName() + " is COOKED!");
                        if (onCooked != null)
                            onCooked.run();
                    }
                }

                Thread.sleep(100); // Check every 100ms
            }

        } catch (InterruptedException e) {
            System.err.println("[COOKING THREAD] Cooking interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        stopped = true;
    }
}
