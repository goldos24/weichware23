package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;

import static org.junit.jupiter.api.Assertions.*;

class EdgePenguinPenaltyTest {
    @Test
    void rate() {
        var gameState = GameStateFixture.createTestGameState();
        Rater rater = new EdgePenguinPenalty();
        Rating expected = new Rating(-4);
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }
}