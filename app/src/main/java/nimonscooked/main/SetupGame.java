package nimonscooked.main;

import nimonscooked.entity.Chef;

public class SetupGame {
    GamePanel gp;

    public SetupGame(GamePanel gp) {
        this.gp = gp;
    }

    public void resetGame() {
        gp.orderManager.reset();

        gp.chefs.clear();
        gp.chefs.add(new Chef("C1", "Kirby", 6, 2));
        gp.chefs.add(new Chef("C2", "Waddle Dee", 8, 5));
        gp.activeChefIndex = 0;
        gp.lastChefSwitchTime = 0;

        // Generate initial orders
        gp.orderManager.generateOrder();
        gp.orderManager.generateOrder();
        gp.lastOrderTime = System.currentTimeMillis();
        System.out.println("[SETUP] Game reset complete");
    }
}
