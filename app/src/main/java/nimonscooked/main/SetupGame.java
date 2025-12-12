package nimonscooked.main;

import nimonscooked.entity.Chef;
<<<<<<< Updated upstream
=======
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.entity.station.CookingStation;
>>>>>>> Stashed changes
import nimonscooked.entity.station.Station;

public class SetupGame {
    GamePanel gp;

    public SetupGame(GamePanel gp) {
        this.gp = gp;
    }

    public void resetGame() {
        // Reset OrderManager (clears orders, score, timer value)
        gp.orderManager.reset();

        // Clear all station contents
        for (int row = 0; row < gp.gameMap.getRows(); row++) {
            for (int col = 0; col < gp.gameMap.getCols(); col++) {
                Station station = gp.gameMap.getStationAt(col, row);
                if (station != null && station.getContainedItem() != null) {
                    station.takeItem(); // Remove item from station
                }
            }
        }

        // Clear all floor items
        for (int row = 0; row < gp.gameMap.getRows(); row++) {
            for (int col = 0; col < gp.gameMap.getCols(); col++) {
                gp.gameMap.pickupItemFromFloor(col, row);
            }
        }

        // Reset chefs (position and inventory)
        gp.chefs.clear();
        gp.chefs.add(new Chef("C1", "Kirby", 6, 2));
        gp.chefs.add(new Chef("C2", "Waddle Dee", 8, 5));
        gp.activeChefIndex = 0;
        gp.lastChefSwitchTime = 0;

        // Clear chef inventories
        for (Chef chef : gp.chefs) {
            chef.setInventory(null);
        }

        // Restart timer countdown
        gp.orderManager.startStageTimer();

        // Generate initial orders
        gp.orderManager.generateOrder();
        gp.orderManager.generateOrder();
        gp.lastOrderTime = System.currentTimeMillis();

<<<<<<< Updated upstream
        System.out.println("[SETUP] Game reset complete - stations cleared, timer restarted");
=======
        // Ensure every CookingStation has exactly one FryingPan (spawn default pans there)
        int placed = 0;
        for (int row = 0; row < gp.gameMap.getRows(); row++) {
            for (int col = 0; col < gp.gameMap.getCols(); col++) {
                Station s = gp.gameMap.getStationAt(col, row);
                if (s instanceof CookingStation) {
                    // If station already has a FryingPan, count it and continue
                    if (s.getContainedItem() instanceof FryingPan) {
                        placed++;
                        continue;
                    }

                    // Remove any non-pan item
                    if (s.getContainedItem() != null) {
                        s.takeItem();
                    }

                    // Place new pan
                    FryingPan pan = new FryingPan(gp);
                    s.placeItem(pan);
                    placed++;
                    System.out.println("[SETUP] Placed FryingPan on cooking station at (" + col + "," + row + ")");
                }
            }
        }

        System.out.println("[SETUP] Game reset complete - placed pans: " + placed);
>>>>>>> Stashed changes
    }
}
