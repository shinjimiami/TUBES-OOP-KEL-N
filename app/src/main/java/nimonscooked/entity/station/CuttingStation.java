package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import nimonscooked.main.GamePanel;

public class CuttingStation extends Station {

    private final int CuttingDuration = 3000;
    private int savedTime = 0;
    private Chef busyChef = null; // Track which chef is cutting

    public CuttingStation(GamePanel gp) {
        super(gp);
        this.name = "Cutting Station";

        if (gp != null) {
            down1 = setup("/stations/cutting_station");
            int tilesWide = 1;
            int tilesHigh = 1;
            this.imageWidth = gp.tileSize * tilesWide;
            this.imageHeight = gp.tileSize * tilesHigh;
            solidArea = new java.awt.Rectangle(0, 0, this.imageWidth, this.imageHeight);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
    }

    public boolean processCut(int timeNeeded) {
        if (!(containedItem instanceof Preparable)) {
            return false;
        }

        Preparable item = (Preparable) containedItem;

        if (item.getState() != IngredientState.RAW) {
            this.savedTime = 0;
            if (busyChef != null) {
                busyChef.setCurrentAction(nimonscooked.enums.ChefStatus.IDLE);
                busyChef = null;
            }
            return false;
        }

        this.savedTime += timeNeeded;

        if (this.savedTime >= CuttingDuration) {
            item.chop();
            this.savedTime = 0;

            // Release chef from BUSY status
            if (busyChef != null) {
                busyChef.setCurrentAction(nimonscooked.enums.ChefStatus.IDLE);
                System.out.println("[CUTTING] " + item.getName() + " has been CHOPPED! Chef is now IDLE.");
                busyChef = null;
            }
            return true;
        }

        return false;
    }

    public void startCutting(Chef chef) {
        if (busyChef == null && containedItem instanceof Preparable) {
            Preparable item = (Preparable) containedItem;
            if (item.getState() == IngredientState.RAW) {
                busyChef = chef;
                chef.setCurrentAction(nimonscooked.enums.ChefStatus.BUSY);
                System.out.println("[CUTTING] Chef is now BUSY cutting " + item.getName());
            }
        }
    }

    public boolean isBusy() {
        return busyChef != null;
    }

    public Chef getBusyChef() {
        return busyChef;
    }

    @Override
    public void interact(Chef player) {
        Item heldItem = player.getInventory();

        // Place item and start cutting automatically
        if (this.containedItem == null && heldItem != null) {
            super.placeItem(player.getInventory());
            player.setInventory(null);
            this.savedTime = 0;

            // Auto-start cutting if it's a RAW ingredient
            if (this.containedItem instanceof Preparable) {
                Preparable item = (Preparable) this.containedItem;
                if (item.getState() == IngredientState.RAW) {
                    startCutting(player);
                }
            }
            return;
        }

        // Plate + Ingredient logic removed (handled by AssemblyStation)

        // Can only take item if not currently cutting
        if (this.containedItem != null && heldItem == null && busyChef == null) {
            player.setInventory(super.takeItem());
            this.savedTime = 0;
        }
    }

    public int getSavedTime() {
        return savedTime;
    }

    public int getCuttingDurationMs() {
        return CuttingDuration;
    }
}