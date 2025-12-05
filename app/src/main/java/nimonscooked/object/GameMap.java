package nimonscooked.object;

public class GameMap {
    private char[][] grid;
    private final int rows = 10;
    private final int cols = 14;

    public GameMap() {
        
        grid = new char[][]{
            {'X', 'X', 'X', 'X', 'X', 'A', 'A', 'I', 'A', 'A', 'X', 'X', 'X', 'X'},
            {'C', '.', '.', 'X', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A'},
            {'I', '.', '.', 'X', 'X', 'R', 'V', '.', '.', 'R', 'X', '.', '.', 'P'},
            {'C', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S'},
            {'I', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'S'},
            {'C', '.', '.', 'X', 'X', 'R', '.', '.', 'V', 'R', 'X', '.', '.', 'A'},
            {'I', '.', '.', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A'},
            {'A', 'W', 'W', 'A', 'X', 'A', '.', '.', '.', 'A', 'X', '.', '.', 'A'},
            {'X', 'X', 'X', 'X', 'X', 'A', 'A', 'I', 'A', 'A', 'X', '.', '.', 'T'},
            {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
        };
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows) {
            return false;
        }
        char tile = grid[y][x];
        return tile == '.' || tile == 'V';
    }

    public char[][] getGrid() {
        return grid;
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}