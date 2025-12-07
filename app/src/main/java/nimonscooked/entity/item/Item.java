package nimonscooked.entity.item;

import nimonscooked.entity.Entity;

public class Item extends Entity {

    public Item(String id, String name, float x, float y) {
        super(id, name, (int) x, (int) y);
    }
}
