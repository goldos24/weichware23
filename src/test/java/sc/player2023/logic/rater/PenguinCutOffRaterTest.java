package sc.player2023.logic.rater;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.rating.PenguinCutOffRater;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PenguinCutOffRaterTest {
    @Test
    void rate() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Rater rater = new PenguinCutOffRater();
        Rating expected = new Rating(-10.1);
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

}