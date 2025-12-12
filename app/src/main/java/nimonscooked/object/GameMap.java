package nimonscooked.object;

import nimonscooked.entity.station.*;
import nimonscooked.entity.station.KitchenUtensilStorage;
import nimonscooked.enums.IngredientType;
import nimonscooked.entity.item.Item;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
    private char[][] grid;
    // Array 2D ini akan menyimpan objek Station yang sebenarnya (bukan cuma huruf)
    private Station[][] stationGrid;

    // Map to store items on floor: Key = "x,y", Value = Item
    private Map<String, Item> floorItems = new HashMap<>();

    private final int rows = 10;
    private final int cols = 14;

    public GameMap() {
        // Layout Map Type C: Burger Map (Sesuai Spesifikasi)
        // M=Meat, B=Bun, H=Cheese, L=Lettuce, O=Tomato, F=FryingPan, P=Plate
        grid = new char[][] {
                { 'X', 'X', 'X', 'X', 'X', 'A', 'A', 'O', 'A', 'A', 'X', 'X', 'X', 'X' },
                { 'C', '.', '.', 'X', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'F' },
                { 'M', '.', '.', 'X', 'X', 'R', 'V', '.', '.', 'R', 'X', '.', '.', 'P' },
                { 'C', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S' },
                { 'B', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S' },
                { 'C', '.', '.', 'X', 'X', 'R', '.', '.', 'V', 'R', 'X', '.', '.', 'A' },
                { 'L', '.', '.', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A' },
                { 'A', 'W', 'W', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A' },
                { 'X', 'X', 'X', 'X', 'X', 'A', 'A', 'H', 'A', 'A', 'X', '.', '.', 'T' },
                { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' }
        };

        // Inisialisasi Objek Station Berdasarkan Grid
        initializeStations();
    }

    private void initializeStations() {
        stationGrid = new Station[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                char tile = grid[y][x];
                String id = "ST-" + x + "-" + y; // ID Unik: misal ST-0-1

                // Factory sederhana untuk membuat objek station yang sesuai
                // TODO: Pass actual GamePanel instance instead of null
                switch (tile) {
                    case 'C' -> stationGrid[y][x] = new CuttingStation(null);
                    case 'R' -> stationGrid[y][x] = new CookingStation(null);
                    case 'W' -> stationGrid[y][x] = new WashingStation(null);
                    case 'S' -> stationGrid[y][x] = new ServingCounter(null, null);
                    case 'M' -> stationGrid[y][x] = new IngredientStorage(null, IngredientType.MEAT);
                    case 'B' -> stationGrid[y][x] = new IngredientStorage(null, IngredientType.BUN);
                    case 'H' -> stationGrid[y][x] = new IngredientStorage(null, IngredientType.CHEESE);
                    case 'L' -> stationGrid[y][x] = new IngredientStorage(null, IngredientType.LETTUCE);
                    case 'O' -> stationGrid[y][x] = new IngredientStorage(null, IngredientType.TOMATO);
                    case 'P' -> stationGrid[y][x] = new PlateStorage(null);
                    case 'F' -> stationGrid[y][x] = new KitchenUtensilStorage(null); // FryingPan storage
                    case 'T' -> stationGrid[y][x] = new TrashStation(null);
                    case 'A' -> stationGrid[y][x] = new AssemblyStation(null);
                    // '.' (Lantai), 'X' (Tembok), 'V' (Spawn) tidak punya objek station
                    default -> stationGrid[y][x] = null;
                }
            }
        }
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows)
            return false;
        char tile = grid[y][x];
        // Chef hanya bisa jalan di Lantai (.) atau Spawn Point (V)
        return tile == '.' || tile == 'V';
    }

    /**
     * Mengambil objek Station asli di koordinat tertentu.
     * Digunakan oleh InteractCommand agar bisa memanggil method interact() asli.
     */
    public Station getStationAt(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows)
            return null;
        return stationGrid[y][x];
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    // Floor item management for throw mechanic
    public boolean placeItemOnFloor(int x, int y, Item item) {
        if (!isWalkable(x, y)) {
            return false;
        }
        String key = x + "," + y;
        floorItems.put(key, item);
        return true;
    }

    public Item getItemOnFloor(int x, int y) {
        String key = x + "," + y;
        return floorItems.get(key);
    }

    public Item pickupItemFromFloor(int x, int y) {
        String key = x + "," + y;
        return floorItems.remove(key);
    }

    public boolean hasItemOnFloor(int x, int y) {
        String key = x + "," + y;
        return floorItems.containsKey(key);
    }
}