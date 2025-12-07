package nimonscooked.entity.station;

import nimonscooked.entity.Chef;
import nimonscooked.entity.item.kitchenutensil.Plate;
import nimonscooked.entity.item.dish.Dish;
import nimonscooked.object.RecipeManager;

public class ServingCounter extends Station {

    public ServingCounter(String id, float x, float y) {
        super(id, "Serving Counter", (int) x, (int) y);
    }

    @Override
    public void interact(Chef player) {
        // Hanya terima Piring (Plate)
        if (player.getHeldItem() instanceof Plate) {
            Plate plate = (Plate) player.getHeldItem();

            // Validasi Resep
            Dish dish = plate.getDish();
            String recipe = RecipeManager.getRecipeName(dish);

            if (recipe != null) {
                System.out.println(">>> BERHASIL MENYAJIKAN: " + recipe + " <<<");
                // TODO: Tambah skor di sini
            } else {
                System.out.println(">>> GAGAL! Resep salah atau mentah. <<<");
                // TODO: Kurangi skor
            }

            // Kembalikan Piring (Harusnya ke PlateStorage, tapi sbg placeholder kita
            // kosongkan tangan chef)
            player.takeItem();
            // Nanti di PlateStorage, piring ini akan muncul lagi dalam keadaan kotor
        }
    }
}