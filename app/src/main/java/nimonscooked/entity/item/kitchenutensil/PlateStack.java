package nimonscooked.entity.item.kitchenutensil;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PlateStack {
    private Deque<Plate> plates;

    public PlateStack(List<Plate> dirtyPlates) {
        plates = new LinkedList<>();
        if (dirtyPlates != null) {
            for (Plate p : dirtyPlates) {
                if (p != null) plates.push(p);
            }
        }
    }

    public Plate removePlate() {
        if (plates.isEmpty()) return null;
        return plates.pop();
    }

    public int getStackSize() {
        return plates.size();
    }

    public boolean isEmpty() { return plates.isEmpty(); }
}