package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.rating.PotentialFishRater;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;

import static org.junit.jupiter.api.Assertions.*;

class PotentialFishRaterTest {

    private static final int OWN_NORMAL_FIELD_COUNT = 7;
    private static final int OPPONENT_NORMAL_FIELD_COUNT = 50;

    @Test
    void getPotentialFishForTeam() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        int expectedRating = BoardFixture.DEFAULT_MORE_FISH_COUNT + OWN_NORMAL_FIELD_COUNT * BoardFixture.DEFAULT_FISH_COUNT;
        Rating expected = new Rating(expectedRating);
        Rating got = PotentialFishRater.getPotentialFishForTeam(testGameState, testGameState.getCurrentTeam());
        assertEquals(expected, got);
    }

    @Test
    void getPotentialFishForTeamTwo() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        int expectedRating = OPPONENT_NORMAL_FIELD_COUNT * BoardFixture.DEFAULT_FISH_COUNT;
        Rating expected = new Rating(expectedRating);
        Rating got = PotentialFishRater.getPotentialFishForTeam(testGameState, Team.TWO);
        assertEquals(expected, got);
    }

    @Test
    void rate() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        Rater rater = new PotentialFishRater();
        Rating expected = new Rating(BoardFixture.DEFAULT_MORE_FISH_COUNT +
                (OWN_NORMAL_FIELD_COUNT - OPPONENT_NORMAL_FIELD_COUNT) * BoardFixture.DEFAULT_FISH_COUNT);
        Rating got = rater.rate(testGameState);
        assertEquals(expected, got);
    }
}
