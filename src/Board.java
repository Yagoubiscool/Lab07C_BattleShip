import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int SIZE = 10;
    private char[][] grid; // ' ' for empty, 'S' for ship (hidden), 'H' for hit, 'M' for miss
    private Ship[][] shipGrid; // Tracks which ship is at each coordinate
    private List<Ship> ships;
    private Random random;

    public Board() {
        grid = new char[SIZE][SIZE];
        shipGrid = new Ship[SIZE][SIZE];
        ships = new ArrayList<>();
        random = new Random();
        initializeBoard();
        placeShips();
    }

    private void initializeBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = ' ';
                shipGrid[r][c] = null;
            }
        }
    }

    // Places ships in descending order {5, 4, 3, 3, 2}[cite: 1]
    private void placeShips() {
        int[] shipSizes = {5, 4, 3, 3, 2};

        for (int size : shipSizes) {
            Ship ship = new Ship(size);
            boolean placed = false;

            while (!placed) {
                boolean horizontal = random.nextBoolean();
                int row = random.nextInt(SIZE);
                int col = random.nextInt(SIZE);

                if (canPlaceShip(ship, row, col, horizontal)) {
                    executeShipPlacement(ship, row, col, horizontal);
                    ships.add(ship);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(Ship ship, int row, int col, boolean horizontal) {
        int size = ship.getSize();

        if (horizontal) {
            if (col + size > SIZE) return false;
            for (int c = 0; c < size; c++) {
                if (grid[row][col + c] != ' ') return false;
            }
        } else {
            if (row + size > SIZE) return false;
            for (int r = 0; r < size; r++) {
                if (grid[row + r][col] != ' ') return false;
            }
        }
        return true;
    }

    private void executeShipPlacement(Ship ship, int row, int col, boolean horizontal) {
        int size = ship.getSize();
        if (horizontal) {
            for (int c = 0; c < size; c++) {
                grid[row][col + c] = 'S';
                shipGrid[row][col + c] = ship;
            }
        } else {
            for (int r = 0; r < size; r++) {
                grid[row + r][col] = 'S';
                shipGrid[row + r][col] = ship;
            }
        }
    }

    // Processes a player shot and registers the hit on the specific ship
    public ShotResult fireShot(int row, int col) {
        if (grid[row][col] == 'H' || grid[row][col] == 'M') {
            return ShotResult.ALREADY_HIT;
        }

        if (grid[row][col] == 'S') {
            grid[row][col] = 'H';
            Ship hitShip = shipGrid[row][col];
            if (hitShip != null) {
                hitShip.registerHit(); // This properly records the hit on the ship!
            }
            return ShotResult.HIT;
        } else {
            grid[row][col] = 'M';
            return ShotResult.MISS;
        }
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public enum ShotResult {
        HIT, MISS, ALREADY_HIT, SUNK
    }
}