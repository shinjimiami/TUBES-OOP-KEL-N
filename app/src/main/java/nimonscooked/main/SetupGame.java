package nimonscooked.main;

import nimonscooked.entity.Chef;
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

        System.out.println("[SETUP] Game reset complete - stations cleared, timer restarted");
    }
}
