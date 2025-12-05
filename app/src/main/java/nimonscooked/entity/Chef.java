package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.Position;
import nimonscooked.entity.item.Item;

public class Chef {
    private String name;
    private Position position;
    private Direction direction;
    private ChefStatus status;
    // Chef can hold one item
    private Item heldItem;

    public Chef(String name, int startX, int startY) {
        this.name = name;
        this.position = new Position(startX, startY);
        this.direction = Direction.RIGHT; // Default hadap kanan
        this.status = ChefStatus.IDLE;
        this.heldItem = null;
    }

    // Logic pindah posisi
    public void move(int deltaX, int deltaY) {
        // Nanti bisa tambah validasi tembok disini
        this.position.setX(this.position.getX() + deltaX);
        this.position.setY(this.position.getY() + deltaY);
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    // Held item helpers (compatibility with old ChefPlayer API)
    public Item getHeldItem() { return heldItem; }

    public Item takeItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    public void placeItem(Item item) {
        this.heldItem = item;
    }

    // Getter
    public Position getPosition() { return position; }
    public Direction getDirection() { return direction; }
    public ChefStatus getStatus() { return status; }
    public String getName() { return name; }
}