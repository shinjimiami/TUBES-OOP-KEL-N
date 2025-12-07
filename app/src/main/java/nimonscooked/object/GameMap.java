package nimonscooked.object;

import java.util.HashMap;
import java.util.Map;
import nimonscooked.entity.station.*;
import nimonscooked.enums.IngredientType;

public class GameMap {
    private char[][] grid;
    private final int rows = 10;
    private final int cols = 14;

    // PENTING: Map untuk menyimpan objek Station berdasarkan posisi "x,y"
    private Map<String, Station> stationMap = new HashMap<>();

    public GameMap() {
        // Layout Map Type C: Burger Map
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

        initializeStations();
    }

    private void initializeStations() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char code = grid[row][col];
                String id = "ST_" + col + "_" + row;

                // Factory sederhana untuk Map Parser
                switch (code) {
                    case 'C' -> addStation(col, row, new CuttingStation(id, col, row));
                    case 'R' -> addStation(col, row, new CookingStation(id, col, row));
                    case 'A' -> addStation(col, row, new AssemblyStation(id, col, row));
                    case 'P' -> addStation(col, row, new PlateStorage(id, col, row));
                    case 'S' -> addStation(col, row, new ServingCounter(id, col, row));
                    case 'W' -> addStation(col, row, new WashingStation(id, col, row));
                    case 'T' -> addStation(col, row, new TrashStation(id, col, row));
                    case 'I' -> {
                        // LOGIKA KHUSUS MAP C (BURGER)
                        // Kita tentukan isi storage berdasarkan posisi koordinat di peta
                        IngredientType type = IngredientType.BUN; // Default

                        if (row == 0 && col == 7)
                            type = IngredientType.MEAT;
                        else if (row == 2 && col == 0)
                            type = IngredientType.BUN;
                        else if (row == 4 && col == 0)
                            type = IngredientType.CHEESE;
                        else if (row == 6 && col == 0)
                            type = IngredientType.TOMATO;
                        else if (row == 8 && col == 7)
                            type = IngredientType.LETTUCE;

                        addStation(col, row, new IngredientStorage(id, col, row, type));
                    }
                }
            }
        }
    }

    private void addStation(int x, int y, Station station) {
        stationMap.put(x + "," + y, station);
    }

    public Station getStationAt(int x, int y) {
        return stationMap.get(x + "," + y);
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows)
            return false;
        char tile = grid[y][x];
        return tile == '.' || tile == 'V';
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