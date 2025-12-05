package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

public class MoveCommand implements Command {
    private final Direction direction;
    private final int deltaX;
    private final int deltaY;
    private final GameMap map;

    public MoveCommand(Direction direction, int dx, int dy, GameMap map) {
        this.direction = direction;
        this.deltaX = dx;
        this.deltaY = dy;
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY) return;

        chef.setDirection(this.direction);
        int nextX = chef.getPosition().getX() + deltaX;
        int nextY = chef.getPosition().getY() + deltaY;

        if (map.isWalkable(nextX, nextY)) {
            chef.move(deltaX, deltaY);
            System.out.println(chef.getName() + " moved to (" + nextX + ", " + nextY + ")");
        }
    }
}