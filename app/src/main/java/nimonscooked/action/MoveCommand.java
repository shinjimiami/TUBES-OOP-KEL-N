package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;
import java.util.List;

public class MoveCommand implements Command {
    private final Direction direction;
    private final int deltaX;
    private final int deltaY;
    private final GameMap map;
    private final List<Chef> chefs;

    public MoveCommand(Direction direction, int dx, int dy, GameMap map, List<Chef> chefs) {
        this.direction = direction;
        this.deltaX = dx;
        this.deltaY = dy;
        this.map = map;
        this.chefs = chefs;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY || chef.isMoving()) {
            return;
        }

        chef.setDirection(this.direction);

        int nextX = chef.getPosition().getX() + deltaX;
        int nextY = chef.getPosition().getY() + deltaY;

        // Map collision
        if (!map.isWalkable(nextX, nextY)) {
            return;
        }

        // Chef collision
        for (Chef c : chefs) {
            if (c == chef) continue; 

            if (c.getPosition().getX() == nextX && c.getPosition().getY() == nextY) {
                return;
            }
        }

        chef.attemptMove(direction, deltaX, deltaY);
        System.out.println(chef.getName() + " moving to (" + nextX + ", " + nextY + ")");
    }
}