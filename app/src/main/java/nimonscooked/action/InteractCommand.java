package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.ChefStatus;
import nimonscooked.entity.station.Station;
import nimonscooked.enums.Direction;
import nimonscooked.object.GameMap;

public class InteractCommand implements Command {
    private GameMap map;

    public InteractCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY) {
            System.out.println(chef.getName() + " is busy!");
            return;
        }
        Station targetStation = getTargetStation(chef);
        if (targetStation == null) {
            System.out.println("Tidak ada station di depan.");
            return;
        }
        targetStation.interact(chef);
    }

    private Station getTargetStation(Chef chef) {
        Direction dir = chef.getDirection();
        int x = chef.getPosition().getX();
        int y = chef.getPosition().getY();

        switch (dir) {
            case UP -> y -= 1;
            case DOWN -> y += 1;
            case LEFT -> x -= 1;
            case RIGHT -> x += 1;
        }

        return map.getStationAt(x, y);
    }
}
