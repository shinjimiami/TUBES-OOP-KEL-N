package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.interfaces.Preparable;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.dish.Dish;

// berfungsi seperti countertop biasa
// namun bisa merakit/menggabungkan beberapa ingredient menjadi sebuah dish
public class AssemblyStation extends Station {

    public AssemblyStation(GamePanel gp) {
        super(gp);
        this.name = "Assembly Station";

        if (gp != null) {
            down1 = setup("/stations/assembly_station");
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
        Item item = player.getInventory();

        System.out.println("[ASSEMBLY] interact called");
        System.out.println("[ASSEMBLY] Player holding: "
                + (item != null ? item.getName() + " (" + item.getClass().getSimpleName() + ")" : "NOTHING"));
        System.out.println("[ASSEMBLY] Station contains: " + (this.containedItem != null
                ? this.containedItem.getName() + " (" + this.containedItem.getClass().getSimpleName() + ")"
                : "NOTHING"));

        // station kosong, bisa meletakkan item apapun
        if (this.containedItem == null) {
            if (item != null) {
                super.placeItem(player.getInventory());
                player.setInventory(null);
                System.out.println("[ASSEMBLY] Item placed on counter");
            }
            return;
        }

        // station ada item, mengambil atau merakit item tersebut

        // mengambil barang
        if (item == null) {
            player.setInventory(super.takeItem());
            System.out.println("[ASSEMBLY] Mengambil item dari counter");
            return;
        }

        // merakit - PLATE + INGREDIENT
        if (this.containedItem instanceof Plate && item instanceof Preparable) {
            Plate plate = (Plate) this.containedItem;
            Preparable ingredient = (Preparable) item;
            Dish dish = plate.getContainedDish();

            System.out.println("[ASSEMBLY] DEBUG: Trying to add " + ingredient.getName() +
                    " (state: " + ingredient.getState() + ") to plate");

            if (dish == null) {
                System.out.println("[ASSEMBLY] ERROR: Plate has no dish container!");
                return;
            }

            if (dish.addComponent(ingredient)) {
                player.setInventory(null);
                System.out.println("[ASSEMBLY] ✓ Added " + ingredient.getName() + " to plate");
                return;
            } else {
                System.out.println(
                        "[ASSEMBLY] ✗ Failed to add " + ingredient.getName() + " to plate (plate full or invalid)");
                return;
            }
        }

        // merakit - FRYING PAN + INGREDIENT (menambah ingredient KE frying pan)
        if (this.containedItem instanceof nimonscooked.interfaces.CookingDevice && item instanceof Preparable) {
            nimonscooked.interfaces.CookingDevice device = (nimonscooked.interfaces.CookingDevice) this.containedItem;
            Preparable ingredient = (Preparable) item;

            System.out.println("[ASSEMBLY] DEBUG: Trying to add " + ingredient.getName() +
                    " (state: " + ingredient.getState() + ") to FryingPan");
            System.out.println("[ASSEMBLY] DEBUG: FryingPan capacity: " + device.capacity() +
                    ", current size: " + device.getContents().size());

            if (device.canAccept(ingredient)) {
                device.addIngredient(ingredient);
                player.setInventory(null);
                System.out.println("[ASSEMBLY] ✓ Added " + ingredient.getName() + " to cooking device");
                return;
            } else {
                System.out.println("[ASSEMBLY] ✗ Cooking device is full or busy");
                return;
            }
        }

        // merakit - PLATE DI MEJA + FRYING PAN DI TANGAN (memindahkan ingredient DARI
        // frying pan KE plate)
        if (this.containedItem instanceof Plate && item instanceof nimonscooked.interfaces.CookingDevice) {
            Plate plate = (Plate) this.containedItem;
            nimonscooked.interfaces.CookingDevice device = (nimonscooked.interfaces.CookingDevice) item;
            Dish dish = plate.getContainedDish();

            System.out.println("[ASSEMBLY] DEBUG: Transfer FryingPan → Plate");
            System.out.println("[ASSEMBLY] DEBUG: FryingPan contents size: " + device.getContents().size());

            // Ambil ingredient pertama dari frying pan
            Preparable ingredientFromPan = device.getFirstIngredient();

            if (ingredientFromPan != null) {
                System.out.println("[ASSEMBLY] DEBUG: Found ingredient: " + ingredientFromPan.getName() +
                        " (state: " + ingredientFromPan.getState() + ")");

                if (dish.addComponent(ingredientFromPan)) {
                    // Hapus ingredient dari frying pan setelah dipindah ke plate
                    device.removeContents(); // Clear frying pan (capacity=1 jadi hanya 1 ingredient)
                    System.out.println("[ASSEMBLY] ✓ Transferred " + ingredientFromPan.getName() +
                            " from frying pan to plate");
                    return;
                } else {
                    System.out.println("[ASSEMBLY] ✗ Cannot add to plate - rejected by Dish.addComponent()");
                    System.out.println("[ASSEMBLY] DEBUG: Ingredient state: " + ingredientFromPan.getState());
                    System.out.println("[ASSEMBLY] DEBUG: canBeChopped: " + ingredientFromPan.canBeChopped() +
                            ", canBeCooked: " + ingredientFromPan.canBeCooked());
                    return;
                }
            } else {
                System.out.println("[ASSEMBLY] ✗ Frying pan is empty (getFirstIngredient returned null)");
                return;
            }
        }

        System.out.println("[ASSEMBLY] Can't combine these items");
    }
}