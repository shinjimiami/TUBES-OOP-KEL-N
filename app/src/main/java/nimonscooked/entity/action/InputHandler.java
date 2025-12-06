package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.Direction;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyEvent;

public class InputHandler {
    private Map<Integer, Command> keyBindings = new HashMap<>();

    public InputHandler() {
        // Setting tombol WASD
        keyBindings.put(KeyEvent.VK_W, new MoveCommand(Direction.UP, 0, -1));
        keyBindings.put(KeyEvent.VK_S, new MoveCommand(Direction.DOWN, 0, 1));
        keyBindings.put(KeyEvent.VK_A, new MoveCommand(Direction.LEFT, -1, 0));
        keyBindings.put(KeyEvent.VK_D, new MoveCommand(Direction.RIGHT, 1, 0));
    }

    public void handleInput(int keyCode, Chef activeChef) {
        Command cmd = keyBindings.get(keyCode);
        if (cmd != null) {
            cmd.execute(activeChef);
        }
    }
}