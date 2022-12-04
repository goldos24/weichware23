package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class WeightedRaterTest {

    @Test
    void getAnalytics() {
        Rater rater = new WeightedRater(5, new StupidRater());
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        String expected = "(0.0=5*StupidRater{0.0})";
        String got = rater.getAnalytics(gameState);
        assertEquals(expected, got);
    }
}