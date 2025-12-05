package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.Item;
import java.util.List;

public abstract class KitchenUtensils extends Item {
    protected List<Preparable> contents;
    
    public KitchenUtensils(String id, String name, float x, float y, List<Preparable> contents) {
        super(id, name, x, y);
        this.contents = contents;
    }

    public abstract List<Preparable> getContents();

    public abstract void removeContents();
}