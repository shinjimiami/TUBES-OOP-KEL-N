package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.main.OrderManager;

public class ServingCounter extends Station {

    private OrderManager orderManager;

    public ServingCounter(String id, float x, float y) {
        super(id, "Serving Counter", x, y);
    }

    // PENTING: Panggil ini di GamePanel/SetupGame setelah membuat objeknya
    public void setOrderManager(OrderManager om) {
        this.orderManager = om;
    }

    @Override
    public void interact(Chef chef) {
        if (!(chef.getHeldItem() instanceof Plate)) {
            return;
        }

        Plate plate = (Plate) chef.getHeldItem();
        boolean served = servePlate(plate);

        // Plate jadi kotor setelah dipakai menyajikan
        plate.setDirty(true);
        String status = served ? "Order served" : "Order failed";
        System.out.println(status);
    }

    // Method helper untuk dipanggil saat Player melakukan "Drop Item" di station ini
    public boolean servePlate(Item item) {
        if (orderManager == null) {
            System.err.println("FATAL: OrderManager belum diset di ServingCounter!");
            return false;
        }

        if (item instanceof Plate) {
            Plate plate = (Plate) item;

            // Validasi isi piring
            boolean success = orderManager.validateDish(plate.getContents());

            // Bersihkan piring setelah disajikan (baik sukses atau gagal)
            // Sesuai spec: piring jadi kotor dan kembali ke storage (logic return to storage dihandle terpisah)
            plate.clearContents();

            return success;
        }

        System.out.println("Hanya bisa menyajikan Plate!");
        return false;
    }
}
