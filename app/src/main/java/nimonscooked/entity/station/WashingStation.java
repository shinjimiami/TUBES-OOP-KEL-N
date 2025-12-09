
package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.kitchenutensil.PlateStack;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class WashingStation extends Station {
    private final int WashDuration = 5000;
    private int savedTime = 0;

    private final Deque<Plate> cleanPlateStack = new LinkedList<>();
    private final Deque<Plate> dirtyPlateStack = new LinkedList<>();

    public WashingStation(GamePanel gp) {
        super(gp);
        this.name = "Washing Station";

        if (gp != null) {
            down1 = setup("/stations/washing_station");
            int tilesWide = 1;
            int tilesHigh = 2;
            this.imageWidth = gp.tileSize * tilesWide;
            this.imageHeight = gp.tileSize * tilesHigh;
            solidArea = new java.awt.Rectangle(0, 0, this.imageWidth, this.imageHeight);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }
    }

    public boolean processWash(int timeNeeded) {
        if (!(containedItem instanceof Plate) || !((Plate) containedItem).isDirty()) {
            this.savedTime = 0;

            // kalo kosong, ambil dirty plate baru
            if (containedItem == null && !dirtyPlateStack.isEmpty()) {
                super.placeItem(dirtyPlateStack.pop());
                return true;
            }
            return false;
        }

        Plate plateToWash = (Plate) containedItem;

        this.savedTime += timeNeeded;

        // cek Selesai
        if (this.savedTime >= WashDuration) {
            plateToWash.setDirty(false);
            plateToWash.clearDish();
            this.savedTime = 0;

            super.takeItem();

            // Pindahkan piring ke stack bersih
            cleanPlateStack.push(plateToWash);
            System.out.println("[WASH] Piring selesai dicuci dan dipindahkan ke tumpukan bersih.");

            // Otomatis mulai mencuci piring kotor berikutnya jika ada
            if (!dirtyPlateStack.isEmpty()) {
                super.placeItem(dirtyPlateStack.pop());
            }

            return true;
        }

        return false;
    }

    @Override
    public void interact(Chef player) {
        Item heldItem = player.getInventory();
        if (heldItem != null) {

            // player membawa Plate kotor tunggal
            if (heldItem instanceof Plate && ((Plate) heldItem).isDirty()) {
                dirtyPlateStack.push((Plate) player.getInventory());
                player.setInventory(null);
                System.out.println("[WASH] Piring kotor tunggal diletakkan.");
            } else {
                return;
            }

            // Setelah item ditaruh, cek apakah area cuci bisa diisi
            if (this.containedItem == null && !dirtyPlateStack.isEmpty()) {
                super.placeItem(dirtyPlateStack.pop());
                this.savedTime = 0;
            }
            return;
        }

        // mengambil piring bersih
        if (heldItem == null && !cleanPlateStack.isEmpty()) {
            player.setInventory(cleanPlateStack.pop());
            System.out.println("[WASH] Mengambil 1 piring bersih.");
            return;
        }

        // mengambil item
        if (heldItem == null && this.containedItem != null) {
            player.setInventory(super.takeItem());
            this.savedTime = 0; // reset
            return;
        }
    }

    public int getSavedTime() {
        return savedTime;
    }

    public int getWashDurationMs() {
        return WashDuration;
    }

    public List<Plate> getDirtyPlates() {
        return new LinkedList<>(dirtyPlateStack);
    }

    public List<Plate> getCleanPlates() {
        return new LinkedList<>(cleanPlateStack);
    }
}
