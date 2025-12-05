package nimonscooked.entity.item;

import nimonscooked.entity.Entity;

public abstract class Item extends Entity {

    public Item(String id, String name, float x, float y) {
        super(id, name, x, y);
    }

    // kalo perlu tambahan yang universal buat item, tambahin di sini
}
