
package nimonscooked.entity.station;

import nimonscooked.entity.Chef;

// berfungsi untuk menghapus item yang dibuang pemain
// item yang dapat dibuang hanya ingredient atau dish pada plate
// item yang dibuang akan dihapus dari game
public class TrashStation extends Station {
    public TrashStation(String id, float x, float y) {
        super(id, "Trash Station", x, y);
    }

    @Override
    public void interact(Chef chef) {
        if (chef.getHeldItem() != null) {
            chef.takeHeldItem(); // buang item yang sedang dipegang
        }

        if (this.containedItem != null) {
            this.takeItem(); // bersihkan isi trash kalau ada
        }
    }
}
