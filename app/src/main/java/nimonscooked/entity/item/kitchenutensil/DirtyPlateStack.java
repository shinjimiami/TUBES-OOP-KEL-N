package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.Item;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a stack of dirty plates that a chef can carry.
 * This is used when a chef takes multiple dirty plates from PlateStorage at
 * once.
 */
public class DirtyPlateStack extends Item {
    private final List<Plate> dirtyPlates;

    public DirtyPlateStack(GamePanel gp) {
        super(gp);
        this.name = "Tumpukan Piring Kotor";
        this.dirtyPlates = new LinkedList<>();
        // Use the plate image for visualization
        down1 = setup("/items/kitchen_utensils/plate");
    }

    /**
     * Adds a dirty plate to this stack
     */
    public void addPlate(Plate plate) {
        if (plate.isDirty()) {
            dirtyPlates.add(plate);
        }
    }

    /**
     * Gets all dirty plates in this stack
     */
    public List<Plate> getPlates() {
        return dirtyPlates;
    }

    /**
     * Returns the number of dirty plates in this stack
     */
    public int getCount() {
        return dirtyPlates.size();
    }

    /**
     * Checks if this stack is empty
     */
    public boolean isEmpty() {
        return dirtyPlates.isEmpty();
    }

    /**
     * Removes and returns one plate from the stack
     */
    public Plate removeOne() {
        if (!dirtyPlates.isEmpty()) {
            return dirtyPlates.remove(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return name + " (" + getCount() + " piring)";
    }
}
