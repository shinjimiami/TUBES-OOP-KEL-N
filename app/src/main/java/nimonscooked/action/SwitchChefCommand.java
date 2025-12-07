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
        // Switch Chef boleh dilakukan kapan saja (termasuk saat BUSY)
        // Chef yang lama akan tetap melanjutkan status BUSY-nya (di-handle oleh
        // Chef.update)
        System.out.println("Switching active chef...");
        gamePanel.switchChef();
    }
}