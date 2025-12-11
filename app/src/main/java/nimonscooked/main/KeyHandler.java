package nimonscooked.main;

import nimonscooked.entity.Chef;
import nimonscooked.object.GameMap;
import nimonscooked.action.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyHandler {
    private Map<Integer, Command> keyBindings = new HashMap<>();

    public KeyHandler(GameMap gameMap, GamePanel panel, List<Chef> chefs) {
        // Movement
        keyBindings.put(KeyEvent.VK_W, new MoveCommand(nimonscooked.enums.Direction.UP, 0, -1, gameMap, chefs));
        keyBindings.put(KeyEvent.VK_S, new MoveCommand(nimonscooked.enums.Direction.DOWN, 0, 1, gameMap, chefs));
        keyBindings.put(KeyEvent.VK_A, new MoveCommand(nimonscooked.enums.Direction.LEFT, -1, 0, gameMap, chefs));
        keyBindings.put(KeyEvent.VK_D, new MoveCommand(nimonscooked.enums.Direction.RIGHT, 1, 0, gameMap, chefs));

        // Basic Actions
        keyBindings.put(KeyEvent.VK_V, new InteractCommand(gameMap));
        keyBindings.put(KeyEvent.VK_C, new PickUpDropCommand(gameMap));
        keyBindings.put(KeyEvent.VK_B, new SwitchChefCommand(panel));

        // Station-specific Actions
        keyBindings.put(KeyEvent.VK_F, new CookingCommand(gameMap));
        keyBindings.put(KeyEvent.VK_X, new CuttingCommand(gameMap));

        // Dash and throw
        keyBindings.put(KeyEvent.VK_SPACE, new DashCommand(gameMap));
        keyBindings.put(KeyEvent.VK_K, new ThrowCommand(gameMap, chefs));
    }

    public void handleKey(int keyCode, Chef activeChef) {
        Command command = keyBindings.get(keyCode);
        if (command != null) {
            command.execute(activeChef);
        }
    }
}

