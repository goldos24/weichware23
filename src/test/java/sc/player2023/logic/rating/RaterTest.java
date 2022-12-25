package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class RaterTest {

    @Test
    void getAnalytics() {
        // Only works with stupid rater if it doesn't override getAnalytics
        Rater rater = new StupidRater();
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        String expected = "StupidRater{0}";
        String got = rater.getAnalytics(gameState);
        assertEquals(expected, got);
    }
}