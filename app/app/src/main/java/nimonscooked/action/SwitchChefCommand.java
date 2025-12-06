package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.main.GamePanel;

public class SwitchChefCommand implements Command {
    private GamePanel gamePanel;

    public SwitchChefCommand(GamePanel panel) {
        this.gamePanel = panel;
    }

    @Override
    public void execute(Chef activeChef) {
        
        gamePanel.switchChef();
    }
}