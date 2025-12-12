
package nimonscooked.entity.station;

import nimonscooked.main.GamePanel;
import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.kitchenutensil.DirtyPlateStack;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class WashingStation extends Station {
    private final int WashDuration = 3000; // 3 seconds (was 5s)
    private int savedTime = 0;
    private Chef busyChef = null; // Track which chef is washing

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

    /**
     * Proses mencuci piring dengan timer. Dipanggil dari GamePanel update loop.
     * 
     * @param timeNeeded waktu yang telah berlalu (dalam ms)
     * @return true jika ada perubahan status
     */
    public boolean processWash(int timeNeeded) {
        // Validasi: Harus ada piring kotor di area cuci dan ada chef yang sedang
        // mencuci
        if (!(containedItem instanceof Plate) || !((Plate) containedItem).isDirty() || busyChef == null) {
            this.savedTime = 0;
            return false;
        }

        Plate plateToWash = (Plate) containedItem;
        this.savedTime += timeNeeded;

        // Cek apakah sudah selesai mencuci (3 detik)
        if (this.savedTime >= WashDuration) {
            // Cuci piring menggunakan method wash()
            plateToWash.wash();
            this.savedTime = 0;

            super.takeItem();

            // Release chef dari status BUSY
            if (busyChef != null) {
                busyChef.setCurrentAction(nimonscooked.enums.ChefStatus.IDLE);
                System.out.println("[WASH] Piring selesai dicuci! Chef kembali IDLE.");
                busyChef = null;
            }

            // Pindahkan piring bersih ke tumpukan
            cleanPlateStack.push(plateToWash);
            System.out.println(
                    "[WASH] Piring bersih dipindahkan ke tumpukan. Total piring bersih: " + cleanPlateStack.size());

            // TIDAK otomatis mulai mencuci berikutnya (chef harus interact lagi)
            // Ini sesuai spesifikasi: chef harus interact untuk memulai washing

            return true;
        }

        return false;
    }

    public void startWashing(Chef chef) {
        if (busyChef == null && containedItem instanceof Plate && ((Plate) containedItem).isDirty()) {
            busyChef = chef;
            chef.setCurrentAction(nimonscooked.enums.ChefStatus.BUSY);
            System.out.println("[WASH] Chef is now BUSY washing plate");
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

        // KONDISI 1: Chef membawa item
        if (heldItem != null) {
            // 1A: Chef membawa DirtyPlateStack (tumpukan piring kotor)
            if (heldItem instanceof DirtyPlateStack) {
                DirtyPlateStack stack = (DirtyPlateStack) heldItem;
                System.out.println("[WASH] Meletakkan " + stack.getCount() + " piring kotor ke washing station.");

                // Pindahkan semua piring dari stack ke dirtyPlateStack
                for (Plate plate : stack.getPlates()) {
                    dirtyPlateStack.push(plate);
                }
                player.setInventory(null);

                // Auto-start washing jika area cuci kosong
                if (this.containedItem == null && !dirtyPlateStack.isEmpty()) {
                    super.placeItem(dirtyPlateStack.pop());
                    this.savedTime = 0;
                    startWashing(player);
                }
                return;
            }

            // 1B: Chef membawa Plate kotor tunggal
            if (heldItem instanceof Plate && ((Plate) heldItem).isDirty()) {
                dirtyPlateStack.push((Plate) heldItem);
                player.setInventory(null);
                System.out.println("[WASH] Piring kotor tunggal diletakkan.");

                // Auto-start washing jika area cuci kosong
                if (this.containedItem == null && !dirtyPlateStack.isEmpty()) {
                    super.placeItem(dirtyPlateStack.pop());
                    this.savedTime = 0;
                    startWashing(player);
                }
                return;
            }

            // 1C: Chef membawa item lain (ditolak)
            System.out.println("[WASH] Washing station hanya menerima piring kotor.");
            return;
        }

        // KONDISI 2: Chef tidak membawa item (tangan kosong)
        if (heldItem == null) {
            // 2A: Ambil piring bersih dari tumpukan (jika tidak sedang mencuci)
            if (!cleanPlateStack.isEmpty() && busyChef == null) {
                player.setInventory(cleanPlateStack.pop());
                System.out.println("[WASH] Mengambil 1 piring bersih.");
                return;
            }

            // 2B: Mulai mencuci jika ada piring kotor di area cuci
            if (this.containedItem instanceof Plate && ((Plate) this.containedItem).isDirty() && busyChef == null) {
                startWashing(player);
                System.out.println("[WASH] Chef mulai mencuci piring (3 detik)...");
                return;
            }

            // 2C: Ambil piring dari area cuci (jika ada dan sudah bersih)
            if (this.containedItem != null && !((Plate) this.containedItem).isDirty()) {
                player.setInventory(super.takeItem());
                this.savedTime = 0;
                System.out.println("[WASH] Mengambil piring bersih dari area cuci.");
                return;
            }

            System.out.println("[WASH] Tidak ada yang bisa dilakukan.");
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

    /**
     * Mendapatkan jumlah piring kotor yang menunggu
     */
    public int getDirtyPlateCount() {
        return dirtyPlateStack.size();
    }

    /**
     * Mendapatkan jumlah piring bersih yang siap diambil
     */
    public int getCleanPlateCount() {
        return cleanPlateStack.size();
    }

    /**
     * Kembalikan semua piring bersih ke PlateStorage
     * Bisa dipanggil saat reset game atau cleanup
     */
    public void returnCleanPlatesToStorage(PlateStorage storage) {
        while (!cleanPlateStack.isEmpty()) {
            storage.addPlate(cleanPlateStack.pop());
        }
        System.out.println("[WASH] Semua piring bersih dikembalikan ke PlateStorage.");
    }
}
