package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.entity.station.Station;
import nimonscooked.entity.station.CuttingStation;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

/**
 * Command untuk manual cutting di CuttingStation
 * Triggered by 'X' key (optional - cutting is auto by default)
 */
public class CuttingCommand implements Command {
    private final GameMap map;

    public CuttingCommand(GameMap map) {
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

        if (targetStation instanceof CuttingStation) {
            CuttingStation cuttingStation = (CuttingStation) targetStation;

            if (cuttingStation.getContainedItem() != null) {
                // Manual trigger cutting process (normally auto)
                cuttingStation.processCut(500); // Add 500ms of cutting progress
                System.out.println("[CUTTING] " + chef.getName() + " is cutting... (" +
                        cuttingStation.getSavedTime() + "ms / " + cuttingStation.getCuttingDurationMs() + "ms)");
            } else {
                System.out.println("[CUTTING] No item to cut!");
            }
        } else {
            System.out.println("[CUTTING] Not facing a cutting station");
        }
    }
}