package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.FryingPan;

// Storage untuk kitchen utensils (FryingPan, dll)
public class KitchenUtensilStorage extends Station {

    public KitchenUtensilStorage(GamePanel gp) {
        super(gp);
        this.name = "Utensil Storage";

        if (gp != null) {
            down1 = setup("/stations/ingredient_storage");
            int tilesWide = 1;
            int tilesHigh = 1;
            this.imageWidth = gp.tileSize * tilesWide;
            this.imageHeight = gp.tileSize * tilesHigh;
            solidArea = new java.awt.Rectangle(0, 0, this.imageWidth, this.imageHeight);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
    }

    @Override
    public void interact(Chef player) {
        Item heldItem = player.getInventory();

        // Kalo tangan chef kosong, bisa ambil FryingPan baru (unlimited)
        if (heldItem == null) {
            FryingPan newPan = new FryingPan(gp);
            player.setInventory(newPan);
            System.out.println("[STORAGE] Took FryingPan");
            return;
        }

        System.out.println("[STORAGE] Player is already holding an item, can't take FryingPan");
    }
}
