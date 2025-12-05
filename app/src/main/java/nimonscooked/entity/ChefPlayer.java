package nimonscooked.entity.station;

import nimonscooked.entity.item.Item;

public class ChefPlayer {
    private Item heldItem;

    public Item getHeldItem() {
        return heldItem;
    }

    public Item takeItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    public void placeItem(Item item) {
        heldItem = item;
    }
}