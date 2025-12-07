package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.interfaces.Preparable;

public class CookingStation extends Station {

    public CookingStation(String id, float x, float y) {
        super(id, "Stove", (int) x, (int) y);
    }

    @Override
    public void interact(Chef player) {
        // Logika 1: Menaruh Pan ke Kompor
        if (this.containedItem == null && player.getHeldItem() instanceof FryingPan) {
            FryingPan pan = (FryingPan) player.takeItem();
            placeItem(pan);

            // Cek isi pan, kalau ada bahan mentah -> Nyalakan api
            if (!pan.isEmpty()) {
                startCookingProcess(pan);
            }
        }
        // Logika 2: Mengambil Pan dari Kompor
        else if (this.containedItem != null && player.getHeldItem() == null) {
            // Matikan kompor kalau diangkat
            if (this.containedItem instanceof FryingPan) {
                ((FryingPan) this.containedItem).stopCooking();
            }
            player.placeItem(takeItem());
        }
    }

    private void startCookingProcess(FryingPan pan) {
        // Thread Memasak (Non-blocking)
        new Thread(() -> {
            try {
                System.out.println("[Kompor] Mulai memasak...");
                pan.startCooking();

                // Fase 1: Tunggu Matang (12 detik)
                for (int i = 0; i < 12; i++) {
                    if (!pan.isCooking())
                        return; // Panci diangkat, stop thread
                    Thread.sleep(1000);
                }

                // Ubah jadi COOKED
                for (Preparable p : pan.getContents()) {
                    p.cook();
                }
                System.out.println("[Kompor] Makanan MATANG (Cooked)!");

                // Fase 2: Tunggu Gosong (12 detik lagi)
                for (int i = 0; i < 12; i++) {
                    if (!pan.isCooking())
                        return;
                    Thread.sleep(1000);
                }

                // Cek apakah panci masih di sini sebelum gosong
                if (this.containedItem == pan) {
                    for (Preparable p : pan.getContents()) {
                        p.cook(); // Cooked -> Burned
                    }
                    System.out.println("[Kompor] Makanan GOSONG (Burned)!");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                pan.stopCooking();
            }
        }).start();
    }
}