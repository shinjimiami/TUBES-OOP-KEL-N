package nimonscooked.entity.station;

import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.main.OrderManager;

public class ServingCounter extends Station {

    public ServingCounter(String id, float x, float y) {
        super(id, "Serving Counter", x, y);
    }
    
    private OrderManager orderManager;

    public ServingCounter() {
        super();
        // ID & Nama bisa diset via constructor super atau setter jika ada
    }

    // PENTING: Panggil ini di GamePanel/SetupGame setelah membuat objeknya
    public void setOrderManager(OrderManager om) {
        this.orderManager = om;
    }

    @Override
    public void interactionType() {
        // Biasanya ServingCounter bereaksi terhadap aksi DROP, bukan Interact biasa.
        // Tapi jika desain Anda menggunakan tombol Interact untuk menyajikan,
        // Anda butuh referensi ke Chef/Player di parameter method ini.
        // Karena signature method abstract-nya void interactionType(), 
        // kita asumsikan validasi dipanggil dari luar (misal dari logic Player).
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