package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.ChefStatus;
import nimonscooked.entity.station.CuttingStation;

public class CuttingCommand implements Command {

    private static final int progressInterval = 1000;

    public CuttingCommand() {
    }

    @Override
    public void execute(Chef chef) {
        if (!(chef.getStationInFront() instanceof CuttingStation)) {
            System.out.println("[CUTTING] Player is not in front of Cutting Station.");
            return;
        }

        CuttingStation station = (CuttingStation) chef.getStationInFront();

        if (station.getContainedItem() == null) {
            System.out.println("[CUTTING] Cutting Station is empty.");
            return;
        }

        chef.setStatus(ChefStatus.BUSY);

        boolean finished = station.processCut(progressInterval);

        if (finished) {
            chef.setStatus(ChefStatus.IDLE);
        }
    }
}