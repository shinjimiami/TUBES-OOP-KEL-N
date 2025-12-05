package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;

public class MoveCommand implements Command {
    private final Direction direction;
    private final int deltaX, deltaY;

    public MoveCommand(Direction direction, int dx, int dy) {
        this.direction = direction;
        this.deltaX = dx;
        this.deltaY = dy;
    }

    @Override
    public void execute(Chef chef) {
        // Jangan gerak kalau lagi sibuk (BUSY)
        if (chef.getStatus() == ChefStatus.BUSY) return;

        chef.setDirection(this.direction);
        chef.move(deltaX, deltaY);
        
        // Debug di terminal
        System.out.println(chef.getName() + " bergerak ke " + direction);
    }
}