package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import nimonscooked.main.GamePanel;
import nimonscooked.entity.item.kitchenutensil.Plate;

public class CuttingStation extends Station {

    private final int CuttingDuration = 3000;
    private int savedTime = 0; 

    public CuttingStation(GamePanel gp) {
        super(gp); 
        this.name = "Cutting Station";

        down1 = setup("/stations/cutting_station");
        int tilesWide = 1;
        int tilesHigh = 1;
        this.imageWidth = gp.tileSize * tilesWide;
        this.imageHeight = gp.tileSize * tilesHigh;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = this.imageWidth;
        solidArea.height = this.imageHeight;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public boolean processCut(int timeNeeded) {
        if (!(containedItem instanceof Preparable)) {
            return false;
        }

        Preparable item = (Preparable) containedItem;
        
        if (item.getState() != IngredientState.RAW) {
            this.savedTime = 0; 
            return false; 
        }

        this.savedTime += timeNeeded;

        if (this.savedTime >= CuttingDuration) {
            item.chop(); 
            this.savedTime = 0; 
            System.out.println("[CUTTING] " + item.getName() + " has been CHOPPED!");
            return true;
        }

        return false;
    }
    
    @Override
    public void interact(Chef player) {
        Item heldItem = player.getHeldItem();
        
        if (this.containedItem == null && heldItem != null) {
            super.placeItem(player.takeItem());
            this.savedTime = 0; 
            return;
        }
        
        if (this.containedItem instanceof Plate && heldItem instanceof Preparable) {
            Plate plate = (Plate) this.containedItem;
            Preparable ingredient = (Preparable) heldItem;
            
            // Asumsi Plate memiliki addComponent
            // if (plate.addComponent(ingredient)) { 
            //     player.takeItem();
            // }
            return;
        }
        
        if (this.containedItem != null && heldItem == null) {
            player.setHeldItem(super.takeItem());
        }
    }

    public int getSavedTime() { return savedTime; }
    public int getCuttingDurationMs() { return CuttingDuration; }
}