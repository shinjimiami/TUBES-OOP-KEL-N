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

            if (dish.addComponent(ingredient)) {
                player.setInventory(null);
                System.out.println("[ASSEMBLY] Added " + ingredient.getName() + " to plate");
                return;
            }
        }

        // merakit - FRYING PAN + INGREDIENT
        if (this.containedItem instanceof nimonscooked.interfaces.CookingDevice && item instanceof Preparable) {
            nimonscooked.interfaces.CookingDevice device = (nimonscooked.interfaces.CookingDevice) this.containedItem;
            Preparable ingredient = (Preparable) item;

            if (device.canAccept(ingredient)) {
                device.addIngredient(ingredient);
                player.setInventory(null);
                System.out.println("[ASSEMBLY] Added " + ingredient.getName() + " to cooking device");
                return;
            } else {
                System.out.println("[ASSEMBLY] Cooking device is full or busy");
                return;
            }
        }

        System.out.println("[ASSEMBLY] Can't combine these items");
    }
}