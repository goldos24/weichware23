package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 11.11.2022
 */
public class RatedMoveTest {

    private final Coordinates from = new Coordinates(2, 3);
    private final Coordinates to = new Coordinates(4, 3);
    private final Move move = new Move(from, to);
    private final Rating rating = Rating.FIVE;
    private final RatedMove ratedMove = new RatedMove(rating, move);

    @Test
    void testEquals() {
        RatedMove expected = new RatedMove(rating, move);
        assertEquals(expected, ratedMove, "equal");
    }

    @Test
    void testSame() {
        assertEquals(ratedMove, ratedMove, "same");
    }

    @Test
    void testDifferent() {
        assertNotEquals(ratedMove, "this is not a ratedMove!", "equal");
    }

    @Test
    void testToString() {
        RatedMove expectedRatedMove = new RatedMove(rating, move);
        String expected = expectedRatedMove.toString();
        String actual = ratedMove.toString();
        assertEquals(expected, actual, "toString");
    }

    @Test
    void compareTo() {
        Coordinates expectedTo = new Coordinates(4, 4);
        Coordinates expectedFrom = new Coordinates(1, 1);
        Move expectedMove = new Move(expectedFrom, expectedTo);
        RatedMove o = new RatedMove(Rating.TWO, expectedMove);
        assertTrue(ratedMove.compareTo(o) >= 1, "compareTo");
    }

    @Test
    void rating() {
        Rating actual = ratedMove.rating();
        assertEquals(rating, actual, "rating()");
    }

    @Test
    void move() {
        Move actual = ratedMove.move();
        assertEquals(move, actual, "move()");
    }
}