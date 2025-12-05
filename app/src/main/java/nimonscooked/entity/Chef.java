package nimonscooked.entity;

import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.object.Position;
import nimonscooked.object.Item;

public class Chef {
    private String id;
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory;
    private ChefStatus currentAction;

    public Chef(String id, String name, int startX, int startY) {
        this.id = id;
        this.name = name;
        this.position = new Position(startX, startY);
        this.direction = Direction.RIGHT;
        this.inventory = null;
        this.currentAction = ChefStatus.IDLE;
    }

    public void move(int deltaX, int deltaY) {
        this.position.setX(this.position.getX() + deltaX);
        this.position.setY(this.position.getY() + deltaY);
    }
    
    // Getter Setter
    public void setDirection(Direction d) { this.direction = d; }
    public Direction getDirection() { return direction; }
    public void setInventory(Item i) { this.inventory = i; }
    public Item getInventory() { return inventory; }
    public void setCurrentAction(ChefStatus s) { this.currentAction = s; }
    public ChefStatus getCurrentAction() { return currentAction; }
    public Position getPosition() { return position; }
    public String getName() { return name; }
}