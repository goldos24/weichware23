package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PenguinCutOffRaterTest {
    @Test
    void rate() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Rater rater = new PenguinCutOffRater();
        Rating expected = new Rating(-101);
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

}
