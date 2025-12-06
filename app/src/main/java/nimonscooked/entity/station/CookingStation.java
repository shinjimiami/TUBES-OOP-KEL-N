package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.FryingPan;
import nimonscooked.interfaces.Preparable;
import nimonscooked.enums.IngredientState;
import javax.swing.Timer;
import java.util.List;

public class CookingStation extends Station {
    // Sesuai Spec: 12 Detik Matang, 24 Detik Gosong
    private final int COOK_TIME = 12000;
    private final int BURN_TIME = 24000; // Total waktu dari awal

    private Timer cookingTimer;
    private int elapsedTime = 0; // dalam ms

    public CookingStation(String id, float x, float y) {
        super(id, "Cooking Station", x, y);
    }

    @Override
    public void interact(Chef chef) {
        // Logic ambil/taruh Frying Pan
        if (this.containedItem == null && chef.getHeldItem() instanceof FryingPan) {
            this.placeItem(chef.takeHeldItem());
            checkAndStartCooking();
        } else if (this.containedItem != null && chef.getHeldItem() == null) {
            // Ambil pan -> Stop masak (logic masak di pause atau reset tergantung spec,
            // disini kita pause/stop)
            stopCooking();
            chef.setHeldItem(this.takeItem());
        }
        // Logic menaruh bahan ke dalam Pan yang sedang di kompor
        else if (this.containedItem instanceof FryingPan && chef.getHeldItem() instanceof Preparable) {
            FryingPan pan = (FryingPan) this.containedItem;
            Preparable ingredient = (Preparable) chef.getHeldItem();

            if (pan.canAccept(ingredient)) {
                pan.addIngredient((Preparable) chef.takeHeldItem());
                checkAndStartCooking();
            }
        }
    }

    private void checkAndStartCooking() {
        if (!(this.containedItem instanceof FryingPan))
            return;

        FryingPan pan = (FryingPan) this.containedItem;
        List<Preparable> contents = pan.getContents();

        if (contents.isEmpty())
            return;

        // Asumsi 1 pan 1 bahan (sesuai FryingPan.java capacity=1)
        Preparable item = contents.get(0);

        if (item.canBeCooked()) {
            startCookingTimer(item);
        }
    }

    private void startCookingTimer(Preparable item) {
        if (cookingTimer != null && cookingTimer.isRunning())
            return;

        System.out.println("Mulai memasak " + item.getName());

        // Update tiap 1 detik untuk cek status
        cookingTimer = new Timer(1000, e -> {
            elapsedTime += 1000;

            // Cek Matang
            if (elapsedTime == COOK_TIME) {
                item.cook(); // Ubah ke COOKED
                System.out.println("Makanan Matang!");
            }

            // Cek Gosong
            if (elapsedTime >= BURN_TIME) {
                item.cook(); // Ubah ke BURNED (logic cook() handle state change)
                System.out.println("Makanan Gosong!");
                stopCooking(); // Stop timer kalau sudah gosong
            }
        });

        cookingTimer.start();
    }

    private void stopCooking() {
        if (cookingTimer != null) {
            cookingTimer.stop();
            cookingTimer = null;
        }
        elapsedTime = 0;
    }
}