package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class EarlyGameRaterTest {
    private static final Rating NON_ZERO_RATING = new Rating(42);
    private static final Rater RATER = new EarlyGameRater(gameState -> NON_ZERO_RATING);

    @Test
    void rateEarlyGame() {
        ImmutableGameState gameState = GameStateFixture.createTestGameStateOneFishPerField();
        Rating got = RATER.rate(gameState);
        assertEquals(NON_ZERO_RATING, got);
    }

    @Test
    void rateNonEarlyGame() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Rating got = RATER.rate(gameState);
        assertEquals(Rating.ZERO, got);
    }
}