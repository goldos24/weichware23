package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UselessPenguinRaterTest {
    @Test
    void rate() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Rater rater = new UselessPenguinRater();
        Rating expected = new Rating(-2);
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

}