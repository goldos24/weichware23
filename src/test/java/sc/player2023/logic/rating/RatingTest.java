package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 30.10.2022
 */
class RatingTest {

    private final Rating rating = new Rating(5);

    @Test
    void addRating() {
        Rating other = new Rating(3);
        Rating expected = new Rating(8);
        Rating actual = rating.add(other);
        assertEquals(expected, actual, "addRating");
    }

    @Test
    void addDouble() {
        int other = 3;
        Rating expected = new Rating(8);
        Rating actual = rating.add(other);
        assertEquals(expected, actual, "addDouble");
    }

    @Test
    void subtractRating() {
        Rating other = new Rating(3);
        Rating expected = new Rating(2);
        Rating actual = rating.subtract(other);
        assertEquals(expected, actual, "subtractRating");
    }

    @Test
    void subtractDouble() {
        int other = 3;
        Rating expected = new Rating(2);
        Rating actual = rating.subtract(other);
        assertEquals(expected, actual, "subtractDouble");
    }

    @Test
    void multiply() {
        int other = 3;
        Rating expected = new Rating(15);
        Rating actual = rating.multiply(other);
        assertEquals(expected, actual, "multiply");
    }

    @Test
    void negate() {
        Rating actual = rating.negate();
        double expectedDouble = -rating.rating();
        Rating expected = new Rating(expectedDouble);
        assertEquals(expected, actual, "negate");
    }

    @Test
    void isGreaterThanTrue() {
        Rating other = new Rating(3);
        boolean actual = rating.isGreaterThan(other);
        assertTrue(actual, "subtractRating");
    }

    @Test
    void isGreaterThanFalse() {
        Rating other = new Rating(6);
        boolean actual = rating.isGreaterThan(other);
        assertFalse(actual, "subtractRating");
    }

    @Test
    void isLessThanTrue() {
        Rating other = new Rating(6);
        boolean actual = rating.isLessThan(other);
        assertTrue(actual, "subtractRating");
    }

    @Test
    void isLessThanFalse() {
        Rating other = new Rating(3);
        boolean actual = rating.isLessThan(other);
        assertFalse(actual, "subtractRating");
    }

    @Test
    void testEquals() {
        Rating expected = new Rating(5);
        assertEquals(expected, rating, "equal");
    }

    @Test
    void testSame() {
        assertEquals(rating, rating, "same");
    }

    @Test
    void testDifferent() {
        assertNotEquals(rating, "This is not a Rating", "different");
    }

    @Test
    void testHashCode() {
        int expected = new Rating(5).hashCode();
        int actual = rating.hashCode();
        assertEquals(expected, actual, "hashCode");

    }

    @Test
    void testToString() {
        String expected = new Rating(5).toString();
        String actual = rating.toString();
        assertEquals(expected, actual, "toString");

    }

    @Test
    void rating() {
        int expected = 5;
        double actual = rating.rating();
        assertEquals(expected, actual, "rating");
    }
}
