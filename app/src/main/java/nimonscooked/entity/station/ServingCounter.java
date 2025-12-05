package nimonscooked.entity.station;

import nimonscooked.entity.item.Item;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.main.OrderManager;

public class ServingCounter extends Station {
    
    private OrderManager orderManager;

    public ServingCounter() {
        super();
    }

    // PENTING: Panggil ini di GamePanel/SetupGame setelah membuat objeknya
    public void setOrderManager(OrderManager om) {
        this.orderManager = om;
    }

    @Override
    public void interactionType() {
    }

\    public boolean servePlate(Item item) {
        if (orderManager == null) {
            System.err.println("FATAL: OrderManager belum diset di ServingCounter!");
            return false;
        }

        if (item instanceof Plate) {
            Plate plate = (Plate) item;
            
            boolean success = orderManager.validateDish(plate.getContents());
            
            plate.clearContents(); 
            
            return success;
        }
        
        System.out.println("Hanya bisa menyajikan Plate!");
        return false;
    }
}