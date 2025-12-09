package nimonscooked.entity.station;

import nimonscooked.entity.station.Station;
import nimonscooked.interfaces.Preparable;
import nimonscooked.interfaces.CookingDevice;
import nimonscooked.enums.IngredientState;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.entity.Chef;
import nimonscooked.main.GamePanel;

import java.util.List;

// berfungsi untuk memasak ingredient yang dapat dimasak
// hanya bisa mulai apabila sudah ada fryingpan di cooking station
public class CookingStation extends Station {
    private final int cookingDuration = 15000;
    private final int burntDuration = 24000;
    private long cookingStartTime = 0;
    private boolean isCooking = false;

    public CookingStation(GamePanel gp) {
        super(gp);
        this.name = "Cooking Station";

        if (gp != null) {
            down1 = setup("/stations/cooking_station");
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
        if (this.containedItem == null && player.getInventory() instanceof CookingDevice) {
            super.placeItem(player.getInventory());
            player.setInventory(null);
        } else if (player.getInventory() == null && this.containedItem != null) {
            player.setInventory(super.takeItem());
        }
    }

    // buat di gamepanel
    public void update(long currentTime) {
        if (!isCooking || getContainedItem() == null)
            return;

        if (!(getContainedItem() instanceof CookingDevice))
            return;

        CookingDevice device = (CookingDevice) getContainedItem();
        Preparable item = device.getFirstIngredient();

        if (item == null)
            return;

        long elapsedTime = currentTime - cookingStartTime;
        IngredientState currentItemState = item.getState();

        // State transitions berdasarkan waktu
        if (elapsedTime >= burntDuration) {
            // Burned state - cook() lagi setelah COOKED
            if (currentItemState == IngredientState.COOKED) {
                item.cook(); // COOKED -> BURNED
                System.out.println("[COOKING] Item BURNED");
            }
        } else if (elapsedTime >= cookingDuration) {
            // Cooked state - cook() pertama kali dari CHOPPED
            if (currentItemState == IngredientState.CHOPPED) {
                item.cook(); // CHOPPED -> COOKED
                System.out.println("[COOKING] Item COOKED - ready to serve!");
            }
        } else if (elapsedTime > 0) {
            // Cooking state (in progress) - perlu casting ke Ingredient untuk set COOKING
            // state
            if (currentItemState == IngredientState.CHOPPED || currentItemState == IngredientState.RAW) {
                if (item instanceof nimonscooked.entity.item.ingredient.Ingredient) {
                    ((nimonscooked.entity.item.ingredient.Ingredient) item).setCurrentState(IngredientState.COOKING);
                    System.out.println("[COOKING] Item COOKING - " + (elapsedTime / 1000) + "s elapsed");
                }
            }
        }
    }

    public void startCooking(long currentTime) {
        if (getContainedItem() == null)
            return;

        if (!(getContainedItem() instanceof CookingDevice))
            return;

        CookingDevice device = (CookingDevice) getContainedItem();
        Preparable item = device.getFirstIngredient();

        if (item == null)
            return;

        // Hanya bisa mulai cooking jika item dalam state RAW atau CHOPPED
        if (item.getState() == IngredientState.RAW || item.getState() == IngredientState.CHOPPED) {
            this.isCooking = true;
            this.cookingStartTime = currentTime;
            device.startCooking();
            System.out.println("[COOKING] Started cooking at " + currentTime);
        }
    }

    public void stopCooking() {
        this.isCooking = false;
        ((CookingDevice) getContainedItem()).stopCooking();
    }

    public boolean isCooking() {
        return isCooking;
    }

    public float getCookingProgress() {
        if (!isCooking || cookingStartTime == 0)
            return 0;
        long elapsed = System.currentTimeMillis() - cookingStartTime;
        return Math.min(1.0f, (float) elapsed / cookingDuration);
    }
}
