package nimonscooked.entity.item;

import java.awt.Rectangle;

import nimonscooked.entity.Entity;
import nimonscooked.main.GamePanel;
import nimonscooked.enums.EntityType;

public class Item extends Entity {

    public Item(String id, String name, int x, int y) {
        super(id, name, x, y);
    }

    public Item(GamePanel gp) {
        super(gp);
        type = EntityType.PICKUP_ITEM;
        collision = true;

        if (gp != null) {
            int tilesWide = 1;
            int tilesHigh = 1;
            solidArea = new Rectangle(0, 0, gp.tileSize * tilesWide, gp.tileSize * tilesHigh);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
}