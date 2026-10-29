import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BattleshipTest {
    private Ship ship;
    private Board board;

    @Before
    public void setUp() {
        ship = new Ship(3);
        board = new Board();
    }

    @Test
    public void testShipInitialization() {
        assertEquals(3, ship.getSize());
        assertEquals(0, ship.getHits());
        assertFalse(ship.isSunk());
    }

    @Test
    public void testShipHitAndSunk() {
        assertFalse(ship.registerHit()); // 1 hit
        assertEquals(1, ship.getHits());
        assertFalse(ship.isSunk());

        assertFalse(ship.registerHit()); // 2 hits
        assertEquals(2, ship.getHits());
        assertFalse(ship.isSunk());

        assertTrue(ship.registerHit()); // 3 hits (Sunk!)
        assertEquals(3, ship.getHits());
        assertTrue(ship.isSunk());
    }

    @Test
    public void testBoardInitialization() {
        assertNotNull(board);
    }

    @Test
    public void testFireShotRange() {
        Board.ShotResult result = board.fireShot(0, 0);
        assertNotNull(result);

        Board.ShotResult secondResult = board.fireShot(0, 0);
        assertEquals(Board.ShotResult.ALREADY_HIT, secondResult);
    }
}