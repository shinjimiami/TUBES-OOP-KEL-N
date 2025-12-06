package nimonscooked.entity.station;

import nimonscooked.entity.Chef; // Pastikan import Chef yang baru
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.ChefStatus;
import javax.swing.Timer;

public class CuttingStation extends Station {
    // Sesuai Spec: 3 Detik
    private final int cuttingDuration = 3000;
    private Timer cuttingTimer;
    private Chef processingChef; // Chef yang sedang memotong

    public CuttingStation(String id, float x, float y) {
        super(id, "Cutting Station", x, y);
    }

    @Override
    public void interact(Chef chef) {
        // A. Jika Station Kosong & Chef bawa item -> Taruh item
        if (this.containedItem == null && chef.getHeldItem() != null) {
            this.placeItem(chef.takeHeldItem());
            return;
        }

        // B. Jika Station Ada Item & Chef tangan kosong -> Ambil item (hanya jika tidak
        // sedang diproses)
        if (this.containedItem != null && chef.getHeldItem() == null && cuttingTimer == null) {
            chef.setHeldItem(this.takeItem());
            return;
        }

        // C. Aksi Memotong (Interact)
        if (this.containedItem != null && this.containedItem instanceof Preparable) {
            Preparable item = (Preparable) this.containedItem;

            // Validasi: Harus RAW agar bisa dicut (sesuai method canBeChopped di
            // Ingredient)
            if (item.canBeChopped()) {
                startCuttingProcess(chef, item);
            }
        }
    }

    private void startCuttingProcess(Chef chef, Preparable item) {
        // Set Chef jadi BUSY
        chef.setStatus(ChefStatus.BUSY);
        this.processingChef = chef;

        System.out.println("Mulai memotong " + item.getName());

        // Timer 3 Detik
        cuttingTimer = new Timer(cuttingDuration, e -> {
            item.chop(); // Ubah state jadi CHOPPED
            System.out.println("Selesai memotong! Jadi: " + item.getName() + " (" + item.getState() + ")");

            // Lepaskan Chef dari status BUSY
            if (processingChef != null) {
                processingChef.setStatus(ChefStatus.IDLE);
                processingChef = null;
            }

            // Matikan timer
            ((Timer) e.getSource()).stop();
            cuttingTimer = null;
        });

        cuttingTimer.setRepeats(false);
        cuttingTimer.start();
    }

    // Method untuk cancel progress jika perlu (opsional, tapi disarankan)
    public void cancelProcess() {
        if (cuttingTimer != null && cuttingTimer.isRunning()) {
            cuttingTimer.stop();
            cuttingTimer = null;
            if (processingChef != null) {
                processingChef.setStatus(ChefStatus.IDLE);
                processingChef = null;
            }
        }
    }
}