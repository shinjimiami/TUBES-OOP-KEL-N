package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.dish.Dish;
import nimonscooked.interfaces.Preparable;
import java.util.ArrayList;
import java.util.List;

public class Plate extends KitchenUtensils {
    private boolean isDirty = false;
    private Dish dish;

    public Plate(GamePanel gp) {
        super(gp, new ArrayList<Preparable>());
        name = "Plate";
        down1 = setup("/items/kitchen_utensils/plate");
        this.dish = new Dish(gp);
    }

    @Override
    public List<Preparable> getContents() {
        return contents;
    }

    public void removeContents() {
        contents.clear();
        dish.clearDish();
    }

    public Dish getContainedDish() {
        return dish;
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clearDish() {
        if (dish != null) {
            dish.clearDish();
        }
    }
}