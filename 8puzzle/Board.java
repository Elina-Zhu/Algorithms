/* *****************************************************************************
 *  Name: Yun Zhu
 *  Date: August 30, 2024
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int n;
    private final int hamming;
    private final int manhattan;
    private final int blankRow;
    private final int blankCol;

    // Create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Tiles array cannot be null");
        }

        this.n = tiles.length;
        this.tiles = new int[n][n];
        int h = 0;
        int m = 0;
        int bR = -1;
        int bC = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    bR = i;
                    bC = j;
                }
                else {
                    if (tiles[i][j] != goalTile(i, j)) {
                        h++;
                    }
                    m += manhattanDistance(i, j, tiles[i][j]);
                }
            }
        }

        this.hamming = h;
        this.manhattan = m;
        this.blankRow = bR;
        this.blankCol = bC;
    }

    // String representation of this board
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Board dimension n
    public int dimension() {
        return n;
    }

    // Number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // Does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;
        return n == board.n && Arrays.deepEquals(tiles, board.tiles);
    }

    // All neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();

        if (blankRow > 0)
            neighbors.add(new Board(swap(blankRow, blankCol, blankRow - 1, blankCol)));
        if (blankRow < n - 1)
            neighbors.add(new Board(swap(blankRow, blankCol, blankRow + 1, blankCol)));
        if (blankCol > 0)
            neighbors.add(new Board(swap(blankRow, blankCol, blankRow, blankCol - 1)));
        if (blankCol < n - 1)
            neighbors.add(new Board(swap(blankRow, blankCol, blankRow, blankCol + 1)));

        return neighbors;
    }

    // A board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copy(tiles);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (twinTiles[i][j] != 0 && twinTiles[i][j + 1] != 0) {
                    swap(twinTiles, i, j, i, j + 1);
                    return new Board(twinTiles);
                }
            }
        }

        return null; // should not happen if n ≥ 2
    }

    // Helper method to determine the goal tile number for a given position
    private int goalTile(int row, int col) {
        return row * n + col + 1;
    }

    // Helper method to calculate the manhattan distance for a tile
    private int manhattanDistance(int row, int col, int tile) {
        if (tile == 0) return 0;
        int goalRow = (tile - 1) / n;
        int goalCol = (tile - 1) % n;
        return Math.abs(row - goalRow) + Math.abs(col - goalCol);
    }

    // Helper method to swap two tiles in a 2D array
    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] newTiles = copy(tiles);
        swap(newTiles, row1, col1, row2, col2);
        return newTiles;
    }

    // Helper method to swap two tiles in place
    private void swap(int[][] oldTiles, int row1, int col1, int row2, int col2) {
        int temp = oldTiles[row1][col1];
        oldTiles[row1][col1] = oldTiles[row2][col2];
        oldTiles[row2][col2] = temp;
    }

    // Helper method to create a deep copy of a 2D array
    private int[][] copy(int[][] oldTiles) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(oldTiles[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    // Unit testing (not graded)
    public static void main(String[] args) {
    }
}
