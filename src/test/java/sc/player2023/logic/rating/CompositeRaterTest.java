package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class CompositeRaterTest {
    @Test
    void getAnalytics() {
        Rater rater = new CompositeRater(new Rater[] {new StupidRater(), new StupidRater()});
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        String expected = "CompositeRater[StupidRater{0.0}, StupidRater{0.0}]";
        String got = rater.getAnalytics(gameState);
        assertEquals(expected, got);
    }
}