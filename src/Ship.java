public class Ship {
    private int size;
    private int hits;
    private boolean sunk;

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
        this.sunk = false;
    }

    // Records a hit on the ship and checks if it is sunk
    public boolean registerHit() {
        if (!sunk) {
            hits++;
            if (hits >= size) {
                sunk = true;
            }
        }
        return sunk;
    }

    public int getSize() {
        return size;
    }

    public int getHits() {
        return hits;
    }

    public boolean isSunk() {
        return sunk;
    }
}