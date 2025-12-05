package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.GameMap;

public class InteractCommand implements Command {
    private GameMap map;

    public InteractCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        if (chef.getCurrentAction() == ChefStatus.BUSY) {
            System.out.println(chef.getName() + " is busy!");
            return;
        }
        startAsyncAction(chef, "Interacting", 3000);
    }

    private void startAsyncAction(Chef chef, String actionName, int durationMs) {
        new Thread(() -> {
            try {
                System.out.println(chef.getName() + " " + actionName + "...");
                chef.setCurrentAction(ChefStatus.BUSY);
                Thread.sleep(durationMs);
                System.out.println("Done " + actionName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                chef.setCurrentAction(ChefStatus.IDLE);
            }
        }).start();
    }
}