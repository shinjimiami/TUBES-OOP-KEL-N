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
        if (chef.getCurrentAction() == ChefStatus.BUSY)
            return;

        int targetX = chef.getPosition().getX();
        int targetY = chef.getPosition().getY();

        switch (chef.getDirection()) {
            case UP -> targetY--;
            case DOWN -> targetY++;
            case LEFT -> targetX--;
            case RIGHT -> targetX++;
        }

        if (targetX < 0 || targetX >= map.getCols() || targetY < 0 || targetY >= map.getRows()) {
            return;
        }

        char tileType = map.getGrid()[targetY][targetX];

        if (tileType == 'T') {
            if (chef.getInventory() != null) {
                System.out.println("[ACTION] Item dibuang ke Trash: " + chef.getInventory().getName());
                chef.setInventory(null); // Hapus item dari inventory
            } else {
                System.out.println("[ACTION] Tangan kosong, tidak ada yang bisa dibuang.");
            }
            return;
        }

        if (tileType == 'I') {
            if (chef.getInventory() == null) {
                Item newItem = getIngredientFromStorage(targetX, targetY);
                if (newItem != null) {
                    chef.setInventory(newItem);
                    System.out.println("[ACTION] Mengambil " + newItem.getName() + " dari Storage.");
                }
            } else {
                System.out.println("[ACTION] Tangan penuh! Tidak bisa mengambil bahan.");
            }
            return;
        }

        System.out.println("[ACTION] Berinteraksi dengan tile: " + tileType + " di (" + targetX + "," + targetY + ")");
    }

    private Item getIngredientFromStorage(int x, int y) {
        // ngambil dari ingredientStorage
        return null;
    }
}