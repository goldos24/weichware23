package sc.player2023.logic.rater;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.UselessPenguinRater;

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
