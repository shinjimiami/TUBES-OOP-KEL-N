
package nimonscooked.entity.station;

import javax.swing.Timer;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.enums.ChefStatus;

public class WashingStation extends Station {
    private final int washDuration = 2000;
    private Timer washingTimer;
    private Chef washingChef;

    public WashingStation(String id, int x, int y) {
        super(id, "Washing Station", x, y);
    }

    @Override
    public void interact(Chef chef) {
        // Place dirty plate to wash
        if (this.containedItem == null && chef.getHeldItem() instanceof Plate) {
            Plate plate = (Plate) chef.takeHeldItem();
            this.placeItem(plate);
            if (plate.isDirty()) {
                startWashing(chef, plate);
            }
            return;
        }

        // Take plate back when washing done and hands are free
        if (this.containedItem instanceof Plate && washingTimer == null && chef.getHeldItem() == null) {
            chef.setHeldItem(this.takeItem());
        }
    }

    private void startWashing(Chef chef, Plate plate) {
        if (washingTimer != null) {
            return;
        }

        washingChef = chef;
        chef.setStatus(ChefStatus.BUSY);

        washingTimer = new Timer(washDuration, e -> {
            plate.setDirty(false);
            if (washingChef != null) {
                washingChef.setStatus(ChefStatus.IDLE);
                washingChef = null;
            }
            washingTimer.stop();
            washingTimer = null;
        });

        washingTimer.setRepeats(false);
        washingTimer.start();
    }
}
