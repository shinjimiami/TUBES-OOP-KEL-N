package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.entity.station.Station;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

public class InteractCommand implements Command {
    private final GameMap map;

    public InteractCommand(GameMap map) {
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

        // 3. Ambil Objek Station dari Map
        Station targetStation = map.getStationAt(targetX, targetY);

        if (targetStation != null) {
            // Panggil logika interaksi spesifik milik station tersebut
            System.out.println(chef.getName() + " interacting with " + targetStation.name);
            targetStation.interact(chef);
        } else {
            System.out.println("Nothing to interact with at (" + targetX + "," + targetY + ")");
        }
    }
}