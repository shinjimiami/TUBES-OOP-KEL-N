package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.kitchenutensil.DirtyPlateStack;

import java.util.LinkedList;
import java.util.Deque;

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

    // Menerima piring kotor dari Serving Counter dan menaruhnya di paling atas
    // stack
    public void receiveDirtyPlate(Plate dirtyPlate) {
        plateStack.push(dirtyPlate);
        System.out.println("[PLATE STORAGE] Dirty plate received! Stack size now: " + plateStack.size());
        System.out.println("[PLATE STORAGE] Clean: " + getCleanPlateCount() + ", Dirty: " + getDirtyPlateCount());
    }

    /**
     * Method untuk menambahkan piring ke storage (bisa bersih/kotor)
     */
    public void addPlate(Plate plate) {
        plateStack.push(plate);
    }

    @Override
    public void interact(Chef player) {
        // KONDISI 3: Station ini TIDAK MENERIMA drop item apapun (sesuai spec)
        if (player.getInventory() != null) {
            System.out.println("[PLATE] Plate Storage hanya untuk mengambil piring, tidak bisa drop item.");
            return;
        }

        if (plateStack.isEmpty()) {
            System.out.println("[PLATE] Tumpukan piring kosong.");
            return;
        }

        Plate topPlate = plateStack.peek();

        if (topPlate.isDirty()) {
            // KONDISI 1: Ambil SEMUA piring kotor yang ada di tumpukan atas
            takeDirtyStack(player);
        } else {
            // KONDISI 2: Ambil HANYA 1 piring bersih dari TOP stack
            Plate cleanPlate = plateStack.pop(); // Ambil dari TOP (last), bukan bottom (first)
            player.setInventory(cleanPlate);
            System.out.println("[PLATE] Mengambil 1 piring bersih dari atas stack.");
        }
    }

    /**
     * Mengambil semua piring kotor dari atas stack secara berurutan
     * sampai ketemu piring bersih atau stack habis
     */
    private void takeDirtyStack(Chef player) {
        DirtyPlateStack dirtyStack = new DirtyPlateStack(gp);

        // Ambil semua piring kotor dari atas sampai ketemu piring bersih
        while (!plateStack.isEmpty() && plateStack.peekFirst().isDirty()) {
            Plate dirtyPlate = plateStack.removeFirst();
            dirtyStack.addPlate(dirtyPlate);
        }

        if (!dirtyStack.isEmpty()) {
            player.setInventory(dirtyStack);
            System.out.println("[PLATE] Mengambil " + dirtyStack.getCount() + " piring kotor.");
        }
    }

    /**
     * Mendapatkan total jumlah piring di storage (bersih + kotor)
     */
    public int getTotalPlateCount() {
        return plateStack.size();
    }

    /**
     * Mendapatkan jumlah piring bersih di storage
     */
    public int getCleanPlateCount() {
        int count = 0;
        for (Plate plate : plateStack) {
            if (!plate.isDirty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Mendapatkan jumlah piring kotor di storage
     */
    public int getDirtyPlateCount() {
        int count = 0;
        for (Plate plate : plateStack) {
            if (plate.isDirty()) {
                count++;
            }
        }
        return count;
    }
}