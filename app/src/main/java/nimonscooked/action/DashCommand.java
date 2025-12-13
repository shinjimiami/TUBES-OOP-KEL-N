package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.object.GameMap;

public class DashCommand implements Command {
    private GameMap map;

    public DashCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef != null && map != null) {
            boolean success = chef.dash(map);
            if (!success) {
                // Optional: Add visual/audio feedback for failed dash
                System.out.println("[DASH] Dash failed for " + chef.getName());
            }
        }
    }
}
