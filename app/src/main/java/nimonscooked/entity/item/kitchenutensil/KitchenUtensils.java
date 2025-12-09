package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.main.GamePanel;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.Item;
import java.util.List;

public abstract class KitchenUtensils extends Item {
    protected List<Preparable> contents;
    
    public KitchenUtensils(GamePanel gp, List<Preparable> contents) {
        super(gp);
        this.contents = contents;
        
    }

    public abstract List<Preparable> getContents();

    public abstract void removeContents();
}