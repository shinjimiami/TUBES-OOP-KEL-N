package nimonscooked.entity.item;

import java.awt.Rectangle;

import nimonscooked.entity.Entity;
import nimonscooked.main.GamePanel;
import nimonscooked.enums.EntityType;

public abstract class Item extends Entity {

    public Item(GamePanel gp) {
        super(gp);
        type = EntityType.PICKUP_ITEM;
        collision = true;

        int tilesWide = 1;
        int tilesHigh = 1;
        solidArea = new Rectangle(0, 0, gp.tileSize * tilesWide, gp.tileSize * tilesHigh);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        

    }

    // kalo perlu tambahan yang universal buat item, tambahin di sini
}
