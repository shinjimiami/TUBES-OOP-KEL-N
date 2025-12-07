package nimonscooked.object;

import nimonscooked.entity.station.*;

public class GameMap {
    private char[][] grid;
    // Array 2D ini akan menyimpan objek Station yang sebenarnya (bukan cuma huruf)
    private Station[][] stationGrid;

    private final int rows = 10;
    private final int cols = 14;

    public GameMap() {
        // Layout Map Type C: Burger Map (Sesuai Spesifikasi)
        grid = new char[][] {
                { 'X', 'X', 'X', 'X', 'X', 'A', 'A', 'I', 'A', 'A', 'X', 'X', 'X', 'X' },
                { 'C', '.', '.', 'X', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A' },
                { 'I', '.', '.', 'X', 'X', 'R', 'V', '.', '.', 'R', 'X', '.', '.', 'P' },
                { 'C', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S' },
                { 'I', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S' },
                { 'C', '.', '.', 'X', 'X', 'R', '.', '.', 'V', 'R', 'X', '.', '.', 'A' },
                { 'I', '.', '.', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A' },
                { 'A', 'W', 'W', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A' },
                { 'X', 'X', 'X', 'X', 'X', 'A', 'A', 'I', 'A', 'A', 'X', '.', '.', 'T' },
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
                switch (tile) {
                    case 'C' -> stationGrid[y][x] = new CuttingStation(id, x, y);
                    case 'R' -> stationGrid[y][x] = new CookingStation(id, x, y);
                    case 'W' -> stationGrid[y][x] = new WashingStation(id, x, y);
                    case 'S' -> stationGrid[y][x] = new ServingCounter(id, x, y);
                    case 'I' -> stationGrid[y][x] = new IngredientStorage(id, x, y);
                    case 'P' -> stationGrid[y][x] = new PlateStorage(id, x, y);
                    case 'T' -> stationGrid[y][x] = new TrashStation(id, x, y);
                    case 'A' -> stationGrid[y][x] = new AssemblyStation(id, x, y);
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
}