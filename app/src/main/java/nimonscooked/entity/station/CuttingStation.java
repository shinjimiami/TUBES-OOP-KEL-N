package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.ChefStatus;

public class CuttingStation extends Station {

    public CuttingStation(String id, float x, float y) {
        super(id, "Cutting Station", (int) x, (int) y);
    }

    @Override
    public void interact(Chef player) {
        // Case 1: Station Kosong, Pemain bawa item -> Taruh item
        if (this.containedItem == null && player.getHeldItem() != null) {
            this.placeItem(player.takeItem());
        }
        // Case 2: Station Ada Item, Pemain tangan kosong -> Ambil item
        else if (this.containedItem != null && player.getHeldItem() == null) {
            // Cek dulu apakah item perlu diproses?
            if (this.containedItem instanceof Preparable) {
                Preparable item = (Preparable) this.containedItem;

                // Jika bisa dipotong, POTONG DULU (Jangan diambil)
                if (item.canBeChopped()) {
                    processCutting(player, item);
                    return; // Keluar, jangan diambil dulu
                }
            }
            // Kalau tidak bisa dipotong (sudah jadi/bukan bahan), ambil.
            player.placeItem(this.takeItem());
        }
    }

    private void processCutting(Chef player, Preparable item) {
        // Set Player jadi BUSY (tidak bisa gerak)
        player.setStatus(ChefStatus.BUSY);
        System.out.println("Mulai memotong " + item.getName() + "...");

        // Jalankan timer di Thread terpisah agar UI tidak freeze total,
        // tapi status player tetap BUSY.
        new Thread(() -> {
            try {
                // Simulasi memotong 3 detik
                Thread.sleep(3000);

                // Ubah state item
                item.chop();
                System.out.println(item.getName() + " selesai dipotong!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Kembalikan status player jadi IDLE (bisa gerak lagi)
                player.setStatus(ChefStatus.IDLE);
            }
        }).start();
    }
}