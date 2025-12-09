package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.main.GamePanel;

public class SwitchChefCommand implements Command {
    private final GamePanel gamePanel;

    public SwitchChefCommand(GamePanel panel) {
        this.gamePanel = panel;
    }

    @Override
    public void execute(Chef activeChef) {

        System.out.println("Switching active chef...");
        gamePanel.switchChef();
    }
}