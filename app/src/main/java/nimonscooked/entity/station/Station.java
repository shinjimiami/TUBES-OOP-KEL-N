
package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.Chef;
import nimonscooked.entity.Entity;
import nimonscooked.enums.EntityType;

public abstract class Station extends Entity {
    private boolean isOccupied;
    protected Item containedItem;

    public Station(GamePanel gp) {
        super(gp);
        this.isOccupied = false;
        this.containedItem = null;
        type = EntityType.INTERACTIVE_OBJECT;
        collision = true;
    }

    abstract void interact(Chef player);

    public Item takeItem(){
        Item item = this.containedItem;
        this.containedItem = null;
        this.isOccupied = false;
        return item;
    }

    public boolean placeItem(Item item){
        if(this.isOccupied){
            return false;
        }
        this.containedItem = item;
        this.isOccupied = true;
        return true;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Item getContainedItem() {
        return containedItem;
    }

    public Item peekItem(){
        return this.containedItem;
    }

    public void setContainedItem(Item containedItem) {
        this.containedItem = containedItem;
    }  
}
