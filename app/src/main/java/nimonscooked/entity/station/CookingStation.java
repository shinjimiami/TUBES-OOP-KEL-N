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
    private final int cookingDuration = 12000; // 12 seconds to COOKED (was 15s)
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
        System.out.println("[COOKING INTERACT] Player inventory: " +
                (player.getInventory() != null ? player.getInventory().getName() : "EMPTY"));
        System.out.println("[COOKING INTERACT] Station contains: " +
                (this.containedItem != null ? this.containedItem.getName() : "EMPTY"));

        // Menaruh FryingPan ke cooking station
        if (this.containedItem == null && player.getInventory() instanceof CookingDevice) {
            super.placeItem(player.getInventory());
            player.setInventory(null);
            System.out.println("[COOKING INTERACT] ✓ Placed FryingPan on station");
        }
        // Mengambil FryingPan dari cooking station
        else if (player.getInventory() == null && this.containedItem != null) {
            // Stop cooking jika sedang cooking
            if (isCooking) {
                stopCooking();
                System.out.println("[COOKING INTERACT] Stopped cooking because item was taken");
            }
            player.setInventory(super.takeItem());
            System.out.println("[COOKING INTERACT] ✓ Took FryingPan from station");
        } else {
            System.out.println("[COOKING INTERACT] ✗ Cannot interact - conditions not met");
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

        // Debug: Log progress setiap frame
        if (elapsedTime % 3000 < 100) { // Log setiap ~3 detik
            System.out.println("[COOKING] DEBUG: Elapsed = " + (elapsedTime / 1000) + "s, State = " + item.getState() +
                    ", CookDuration = " + (cookingDuration / 1000) + "s, BurntDuration = " + (burntDuration / 1000)
                    + "s");
        }
        IngredientState currentItemState = item.getState();

        // State transitions berdasarkan waktu
        if (elapsedTime >= burntDuration) {
            // Burned state - cook() lagi setelah COOKED
            if (currentItemState == IngredientState.COOKED) {
                item.cook(); // COOKED -> BURNED
                System.out.println("[COOKING] Item BURNED");
                System.out.println("[COOKING] DEBUG: FryingPan contents after BURNED: " + device.getContents().size());
            }
        } else if (elapsedTime >= cookingDuration) {
            // Cooked state - cook() pertama kali dari CHOPPED atau COOKING
            // ✅ FIX: Accept both CHOPPED and COOKING states
            if (currentItemState == IngredientState.CHOPPED || currentItemState == IngredientState.COOKING) {
                item.cook(); // CHOPPED/COOKING -> COOKED
                System.out.println("[COOKING] Item COOKED - ready to serve!");
                System.out.println("[COOKING] DEBUG: FryingPan contents after COOKED: " + device.getContents().size());
            }
        } else if (elapsedTime > 0) {
            // Cooking state (in progress) - perlu casting ke Ingredient untuk set COOKING
            // state
            if (currentItemState == IngredientState.CHOPPED || currentItemState == IngredientState.RAW) {
                if (item instanceof nimonscooked.entity.item.ingredient.Ingredient) {
                    ((nimonscooked.entity.item.ingredient.Ingredient) item).setCurrentState(IngredientState.COOKING);
                    // Log hanya setiap 5 detik untuk mengurangi spam
                    if (elapsedTime % 5000 < 1000) {
                        System.out.println("[COOKING] Progress: " + (elapsedTime / 1000) + "s / "
                                + (cookingDuration / 1000) + "s");
                    }
                }
            }
        }
    }

    public void startCooking(long currentTime) {
        if (getContainedItem() == null) {
            System.out.println("[COOKING] DEBUG: No item in cooking station");
            return;
        }

        if (!(getContainedItem() instanceof CookingDevice)) {
            System.out.println(
                    "[COOKING] DEBUG: Item is not a cooking device: " + getContainedItem().getClass().getSimpleName());
            return;
        }

        CookingDevice device = (CookingDevice) getContainedItem();
        Preparable item = device.getFirstIngredient();

        if (item == null) {
            System.out.println("[COOKING] DEBUG: Cooking device is empty");
            return;
        }

        // Hanya bisa mulai cooking jika item dalam state RAW atau CHOPPED
        IngredientState itemState = item.getState();
        System.out.println("[COOKING] DEBUG: Item state = " + itemState + ", Name = " + item.getName());

        if (itemState == IngredientState.RAW || itemState == IngredientState.CHOPPED) {
            this.isCooking = true;
            this.cookingStartTime = currentTime;
            device.startCooking();
            System.out.println("[COOKING] ✓ Started cooking " + item.getName() + " (state: " + itemState + ")");
        } else {
            System.out.println("[COOKING] ✗ Cannot cook - wrong state: " + itemState);
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
