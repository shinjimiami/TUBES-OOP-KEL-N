package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.Direction;
import nimonscooked.main.GamePanel;
import nimonscooked.object.GameMap;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyEvent;

public class InputHandler {
    private Map<Integer, Command> keyBindings = new HashMap<>();

    public InputHandler(GameMap gameMap, GamePanel panel) {
        keyBindings.put(KeyEvent.VK_W, new MoveCommand(Direction.UP, 0, -1, gameMap));
        keyBindings.put(KeyEvent.VK_S, new MoveCommand(Direction.DOWN, 0, 1, gameMap));
        keyBindings.put(KeyEvent.VK_A, new MoveCommand(Direction.LEFT, -1, 0, gameMap));
        keyBindings.put(KeyEvent.VK_D, new MoveCommand(Direction.RIGHT, 1, 0, gameMap));
        
        keyBindings.put(KeyEvent.VK_V, new InteractCommand(gameMap));
        keyBindings.put(KeyEvent.VK_C, new PickUpDropCommand(gameMap));
        keyBindings.put(KeyEvent.VK_B, new SwitchChefCommand(panel));
    }

    public void handleInput(int keyCode, Chef activeChef) {
        Command command = keyBindings.get(keyCode);
        if (command != null) {
            command.execute(activeChef);
        }
    }
}