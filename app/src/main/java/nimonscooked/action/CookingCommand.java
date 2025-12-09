
package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.entity.station.Station;
import nimonscooked.entity.station.CookingStation;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

/**
 * Command untuk start/stop cooking di CookingStation
 * Triggered by 'C' key
 */
public class CookingCommand implements Command {
    private final GameMap map;

    public CookingCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        // 1. Validasi Status Chef
        if (chef.getCurrentAction() == ChefStatus.BUSY) {
            System.out.println(chef.getName() + " is busy!");
            return;
        }

        // 2. Hitung Posisi Depan Chef
        int targetX = chef.getPosition().getX();
        int targetY = chef.getPosition().getY();

        switch (chef.getDirection()) {
            case UP -> targetY--;
            case DOWN -> targetY++;
            case LEFT -> targetX--;
            case RIGHT -> targetX++;
        }

        // 3. Ambil Station dari Map
        Station targetStation = map.getStationAt(targetX, targetY);

        if (targetStation instanceof CookingStation) {
            CookingStation cookingStation = (CookingStation) targetStation;

            if (cookingStation.isCooking()) {
                // Stop cooking
                cookingStation.stopCooking();
                System.out.println("[COOKING] " + chef.getName() + " stopped cooking");
            } else if (cookingStation.getContainedItem() != null) {
                // Start cooking
                cookingStation.startCooking(System.currentTimeMillis());
                System.out.println("[COOKING] " + chef.getName() + " started cooking");
            } else {
                System.out.println("[COOKING] No item to cook!");
            }
        } else {
            System.out.println("[COOKING] Not facing a cooking station");
        }
    }
}