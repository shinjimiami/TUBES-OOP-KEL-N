package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.ChefStatus;
import nimonscooked.entity.station.Station;
import nimonscooked.enums.Direction;
import nimonscooked.object.GameMap;

public class PickUpDropCommand implements Command {
    private GameMap map;

    public PickUpDropCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY)
            return;

        Station target = getTargetStation(chef);
        if (target == null) {
            System.out.println("Tidak ada station di depan.");
            return;
        }

        target.interact(chef);
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
