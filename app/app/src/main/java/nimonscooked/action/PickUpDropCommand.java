package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

public class PickUpDropCommand implements Command {
    private GameMap map;

    public PickUpDropCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY) return;
        System.out.println("PickUp/Drop Action Triggered");
    }
}