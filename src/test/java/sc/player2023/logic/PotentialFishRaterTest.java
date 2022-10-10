package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PotentialFishRaterTest {

    @Test
    void rate() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        Rater rater = new PotentialFishRater();
        assertEquals(BoardFixture.DEFAULT_MORE_FISH_COUNT + 7 * BoardFixture.DEFAULT_FISH_COUNT, rater.rate(testGameState));
    }
}