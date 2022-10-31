package sc.player2023.comparison;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Till Fransson
 * @since 31.10.2022
 */
class BattleDataTest {

    private final ImmutableGameState gameState = GameStateFixture.createTestGameState();
    private final int runs = 2;

    private final BattleData data = new BattleData(runs, gameState);

    @Test
    void testEquals() {
        BattleData expected = new BattleData(runs, gameState);
        assertEquals(expected, data, "equal");
    }

    @Test
    void testSame() {
        assertEquals(data, data, "same");
    }

    @Test
    void testDifferent() {
        assertNotEquals(data, "This is not BattleData!", "different");
    }

    @Test
    void testHashCode() {
        BattleData expectedData = new BattleData(runs, gameState);
        int expected = expectedData.hashCode();
        int actual = data.hashCode();
        assertEquals(expected, actual, "hashCode");
    }

    @Test
    void testToString() {
        BattleData expectedData = new BattleData(runs, gameState);
        String expected = expectedData.toString();
        String actual = data.toString();
        assertEquals(expected, actual, "toString");
    }

    @Test
    void runs() {
        int actual = data.runs();
        assertEquals(runs, actual, "runs()");
    }

    @Test
    void testBoard() {
        assertEquals(gameState, data.testBoard(), "testBoard");
    }
}
