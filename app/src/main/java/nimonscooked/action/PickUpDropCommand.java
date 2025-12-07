package nimonscooked.action;

import nimonscooked.entity.Chef;
import nimonscooked.object.GameMap;
import nimonscooked.entity.item.Item;
import nimonscooked.enums.Direction;
import nimonscooked.enums.ChefStatus;
import nimonscooked.enums.IngredientState;
import nimonscooked.entity.item.ingredient.*; // Import semua ingredient (Bun, Meat, Cheese, etc)

public class PickUpDropCommand implements Command {
    private final GameMap map;

    public PickUpDropCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute(Chef chef) {
        // Jangan lakukan aksi jika Chef sedang sibuk (misal: memotong)
        if (chef.getCurrentAction() == ChefStatus.BUSY)
            return;

        // 1. Hitung koordinat di depan Chef
        int targetX = chef.getPosition().getX();
        int targetY = chef.getPosition().getY();

        switch (chef.getDirection()) {
            case UP -> targetY--;
            case DOWN -> targetY++;
            case LEFT -> targetX--;
            case RIGHT -> targetX++;
        }

        // 2. Cek Validasi Koordinat Map
        if (targetX < 0 || targetX >= map.getCols() || targetY < 0 || targetY >= map.getRows()) {
            return;
        }

        char tileType = map.getGrid()[targetY][targetX];

        // 3. Logika TRASH STATION ('T') - Membuang Item
        if (tileType == 'T') {
            if (chef.getInventory() != null) {
                System.out.println("Item dibuang ke Trash: " + chef.getInventory().getName());
                chef.setInventory(null); // Hapus item dari inventory
            } else {
                System.out.println("Tangan kosong, tidak ada yang bisa dibuang.");
            }
            return;
        }

        // 4. Logika INGREDIENT STORAGE ('I') - Mengambil Bahan Mentah
        if (tileType == 'I') {
            if (chef.getInventory() == null) {
                Item newItem = getIngredientFromStorage(targetX, targetY);
                if (newItem != null) {
                    chef.setInventory(newItem);
                    System.out.println("Mengambil " + newItem.getName() + " dari Storage.");
                }
            } else {
                System.out.println("Tangan penuh! Tidak bisa mengambil bahan.");
            }
            return;
        }

        // 5. Placeholder untuk Station Lain (Counter/Cutting/Cooking)
        // Nanti kita akan tambahkan logika interaksi dengan objek Station di sini
        // setelah logic StationManager tersedia.
        System.out.println("Berinteraksi dengan tile: " + tileType + " di (" + targetX + "," + targetY + ")");
    }

    // Helper untuk menentukan bahan apa yang keluar berdasarkan posisi Storage (Map
    // C)
    private Item getIngredientFromStorage(int x, int y) {
        // ID ingredient dibuat unik menggunakan timestamp/random sederhana untuk
        // sementara
        String uniqueId = "ING-" + System.currentTimeMillis();

        // Mapping Posisi untuk MAP TYPE C (Burger)
        // Baris 0, Kolom 7 -> Bun
        if (y == 0 && x == 7)
            return new Bun(uniqueId, x, y, IngredientState.RAW);

        // Baris 2, Kolom 0 -> Meat
        if (y == 2 && x == 0)
            return new Meat(uniqueId, x, y, IngredientState.RAW);

        // Baris 4, Kolom 0 -> Lettuce
        if (y == 4 && x == 0)
            return new Lettuce(uniqueId, x, y, IngredientState.RAW);

        // Baris 6, Kolom 0 -> Tomato
        if (y == 6 && x == 0)
            return new Tomato(uniqueId, x, y, IngredientState.RAW);

        // Baris 8, Kolom 7 -> Cheese
        if (y == 8 && x == 7)
            return new Cheese(uniqueId, x, y, IngredientState.RAW);

        // Default fallback (jika ada storage lain)
        return new Bun(uniqueId, x, y, IngredientState.RAW);
    }
}