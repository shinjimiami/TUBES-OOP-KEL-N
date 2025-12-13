package nimonscooked.entity.item.kitchenutensil;

import nimonscooked.main.GamePanel;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.Item;
import java.util.List;

/**
 * Abstract KitchenUtensils class demonstrating GENERICS usage
 * Uses generic List<Preparable> to store various ingredient types
 * This allows type-safe operations on different ingredient implementations
 */
public abstract class KitchenUtensils extends Item {
    protected List<Preparable> contents;

    public KitchenUtensils(GamePanel gp, List<Preparable> contents) {
        super(gp);
        this.contents = contents;

    }

    /**
     * Generic method to get contents with type safety
     * 
     * @return List of Preparable items (type-safe generic collection)
     */
    public java.util.List<nimonscooked.interfaces.Preparable> getContents() {
        return contents;
    }

    public void clearContents() {
        if (contents != null) {
            contents.clear();
        }
    }
}