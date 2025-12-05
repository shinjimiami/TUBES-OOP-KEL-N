package nimonscooked.entity.item.kitchenutensil;
import nimonscooked.entity.item.Item;

public abstract class KitchenUtensils extends Item {
    protected List<Preparable> contents;
    
    public KitchenUtensils(String id, String name, float x, float y) {
        super(id, name, x, y);
    }

    // Tambahkan method ini agar ServingCounter bisa melihat isi piring
    public java.util.List<nimonscooked.interfaces.Preparable> getContents() {
        return contents;
    }

    // Tambahkan method ini untuk mengosongkan piring setelah disajikan
    public void clearContents() {
        if (contents != null) {
            contents.clear();
        }
    }
}