package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.object.GameMap;
import java.util.List;

public class ThrowCommand implements Command {
    private GameMap map;
    private List<Chef> allChefs;

    public ThrowCommand(GameMap map, List<Chef> allChefs) {
        this.map = map;
        this.allChefs = allChefs;
    }

    @Override
    public void execute(Chef chef) {
        if (chef != null && map != null && allChefs != null) {
            boolean success = chef.throwItem(map, allChefs);
            if (!success) {
                // Optional: Add visual/audio feedback for failed throw
                System.out.println("[THROW] Throw failed for " + chef.getName());
            }
        }
    }
}
