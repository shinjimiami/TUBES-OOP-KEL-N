package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.Item;

import java.util.LinkedList;
import java.util.Deque;
import java.util.List;

// berfungsi untuk menyimpan plate yang diambil pemain
// pada awal game, plate akan tersedia di plate storage dalam kondisi bersih
// tetapi apabila telah 
public class PlateStorage extends Station {

    private final Deque<Plate> plateStack = new LinkedList<>();

    public PlateStorage(GamePanel gp) {
        super(gp);
        this.name = "Plate Storage";

        if (gp != null) {
            down1 = setup("/stations/plate_storage");
            int tilesWide = 1;
            int tilesHigh = 1;
            this.imageWidth = gp.tileSize * tilesWide;
            this.imageHeight = gp.tileSize * tilesHigh;
            solidArea = new java.awt.Rectangle(0, 0, this.imageWidth, this.imageHeight);
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
        }

        for (int i = 0; i < 10; i++) {
            plateStack.add(new Plate(gp)); // add clean plates
        }
    }

    // menerima piring kotor dari Serving Counter dan menaruhnya di paling atas.
    public void receiveDirtyPlate(Plate dirtyPlate) {
        plateStack.push(dirtyPlate);
        System.out.println("[PLATE] Dirty plate received in Plate Storage.");
    }

    // Expose current available plates count for UI overlays
    public int getCount() {
        return plateStack.size();
    }

    @Override
    public void interact(Chef player) {
        // Tidak dapat melakukan drop item apapun pada station ini
        if (player.getInventory() != null) {
            System.out.println("[PLATE] Plate Storage hanya untuk mengambil piring.");
            return;
        }

        if (plateStack.isEmpty()) {
            System.out.println("[PLATE] Tumpukan piring kosong.");
            return;
        }

        Plate topPlate = plateStack.peek();

        if (topPlate.isDirty()) {
            // Piring kotor dapat langsung diambil semuanya (stacking)
            takeDirtyStack(player);
        } else {
            // Piring bersih hanya bisa diambil 1 per 1
            Plate cleanPlate = plateStack.removeFirst();
            player.setInventory(cleanPlate);
            System.out.println("[PLATE] Mengambil 1 piring bersih.");
        }
    }

    private void takeDirtyStack(Chef player) {
        List<Plate> dirtyStack = new LinkedList<>();

        while (!plateStack.isEmpty() && plateStack.peekFirst().isDirty()) {
            dirtyStack.add(plateStack.removeFirst());
        }

        if (!dirtyStack.isEmpty()) {
            player.setInventory(dirtyStack.removeFirst());
            System.out.println("[PLATE] Take " + dirtyStack.size() + " dirty plates.");
        }
    }
}