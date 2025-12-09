package nimonscooked.entity;

import nimonscooked.main.GamePanel;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.enums.EntityType;
import nimonscooked.object.Position;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.station.Station;

public class Chef extends Entity {
    private ChefStatus status;
    private Item heldItem;

    public Chef(GamePanel gp) {
        super(gp);
        this.name = "Chef";
        this.type = EntityType.PLAYER;
        this.direction = Direction.DOWN;
        this.status = ChefStatus.IDLE;
        this.heldItem = null;
        this.x = 0;
        this.y = 0;

        down1 = setup("/chef/down1");
        up1 = setup("/chef/up1");
        left1 = setup("/chef/left1");
        right1 = setup("/chef/right1");
    }

    public Chef(String name, int startX, int startY) {
        super(null);
        this.name = name;
        this.x = startX;
        this.y = startY;
        this.direction = Direction.DOWN;
        this.status = ChefStatus.IDLE;
        this.heldItem = null;
        this.type = EntityType.PLAYER;
    }

    @Override
    public void update() {
        super.update();
        if (direction == Direction.UP) y -= 2;
        else if (direction == Direction.DOWN) y += 2;
        else if (direction == Direction.LEFT) x -= 2;
        else if (direction == Direction.RIGHT) x += 2;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public Item takeItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    public void placeItem(Item item) {
        this.heldItem = item;
    }

    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    public void setDirection(Direction d) {
        this.direction = d;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }


    public Station getStationInFront() {
        return null;
        // bakal diganti
    }

    public ChefStatus getStatus() {
        return status;
    }

    public void setStatus(ChefStatus status) {
        this.status = status;
    }
}