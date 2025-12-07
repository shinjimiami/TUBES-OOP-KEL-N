package nimonscooked.entity.item.kitchenutensil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nimonscooked.interfaces.CookingDevice;
import nimonscooked.interfaces.Preparable;

public class FryingPan extends KitchenUtensils implements CookingDevice {

    private static final int MAX_CAPACITY = 3;
    private boolean cooking = false;

    public FryingPan(String id, float x, float y) {
        super(id, "Frying Pan", x, y, new ArrayList<Preparable>());
    }

    @Override
    public boolean isPortable() {
        return true;
    }

    @Override
    public int capacity() {
        return MAX_CAPACITY;
    }

    @Override
    public boolean canAccept(Preparable ingredient) {
        return ingredient != null && ingredient.canBeCooked() && contents.size() < MAX_CAPACITY;
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            contents.add(ingredient);
        }
    }

    @Override
    public void startCooking() {
        this.cooking = true;
    }

    @Override
    public void stopCooking() {
        this.cooking = false;
    }

    @Override
    public void removeContents() {
        contents.clear();
    }

    @Override
    public List<Preparable> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public boolean isCooking() {
        return cooking;
    }
}
