package nimonscooked.entity.item.kitchenutensil;
import nimonscooked.entity.item.Item;

public abstract class KitchenUtensils extends Item {
    protected List<Preparable> contents;
    
    public KitchenUtensils(String id, String name, float x, float y) {
        super(id, name, x, y);
    }
}