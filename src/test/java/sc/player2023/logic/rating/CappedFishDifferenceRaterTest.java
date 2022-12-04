package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.GameScore;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CappedFishDifferenceRaterTest {

    @Test
    void rate() {
        Rater rater = new CappedFishDifferenceRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(10, 20),
                                                              new BoardPeek(BoardFixture.createTestBoard()),
                                                              Team.ONE);

        Rating expected = Rating.FIVE.negate();
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

    @Test
    void rateNoCap() {
        Rater rater = new CappedFishDifferenceRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(10, 12),
                                                              new BoardPeek(BoardFixture.createTestBoard()),
                                                              Team.ONE);

        Rating expected = Rating.TWO.negate();
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }
}