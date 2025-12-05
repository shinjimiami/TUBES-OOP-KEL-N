package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.entity.item.dish.Dish;
import nimonscooked.interfaces.Preparable;
import java.util.ArrayList;
import java.util.List;

public class Plate extends KitchenUtensils {
    private boolean isDirty = false;
    private Dish dish;

    public Plate(String id, float x, float y) {
        super(id, "Plate", x, y, new ArrayList<Preparable>());
        this.dish = new Dish();
    }

    @Override
    public List<Preparable> getContents() {
        return contents;
    }

    @Override
    public void removeContents() {
        contents.clear();
        dish.clearDish();
    }

    public Dish getDish() {
        return dish;
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    public boolean isDirty() {
        return isDirty;
    }
}