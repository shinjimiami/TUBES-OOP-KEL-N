
package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.ingredient.Ingredient;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.kitchenutensil.FryingPan;

// berfungsi untuk menghapus item yang dibuang pemain
// item yang dapat dibuang hanya ingredient atau dish pada plate
// item yang dibuang akan dihapus dari game
public class TrashStation extends Station {
    public TrashStation(GamePanel gp) {
        super(gp);
        this.name = "Trash Station";
    }

    @Override
    public void interact(Chef player) {
        Item item = player.getInventory();

        if (item == null) {
            System.out.println("[TRASH] Nothing to throw away");
            return;
        }
        if (item instanceof Plate) {
            Plate plate = (Plate) item;
            plate.setDirty(true);
            plate.clearDish();
            System.out.println("[TRASH] cleared Dish");
        } else if (item instanceof FryingPan) {
            FryingPan pan = (FryingPan) item;
            pan.removeContents();
            System.out.println("[TRASH] cleared FryingPan");
        } else if (item instanceof Ingredient) {
            player.setInventory(null);
            System.out.println("[TRASH] threw away ingredient");
        } else {
            System.out.println("[TRASH] this can't be thrown away");
        }
    }
}
