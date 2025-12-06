
package nimonscooked.entity.station;

import nimonscooked.entity.item.Item;
import nimonscooked.entity.station.ChefPlayer;
import nimonscooked.entity.Entity;

public abstract class Station extends Entity {
    private boolean isOccupied;
    protected Item containedItem;

    public Station(String id, String name, float x, float y) {
        super(id, name, x, y);
        this.isOccupied = false;
        this.containedItem = null;
    }

    abstract void interact(ChefPlayer player);

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
