package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.BoardFixture;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class CappedFishDifferenceRaterTest {

    @Test
    void rate() {
        Rater rater = new CappedFishDifferenceRater();
        ImmutableGameState gameState = new ImmutableGameState(new Integer[]{10, 20},
                new BoardPeek(BoardFixture.createTestBoard()),
                Team.ONE);

        Rating expected = Rating.FIVE.negate();
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

    @Test
    void rateNoCap() {
        Rater rater = new CappedFishDifferenceRater();
        ImmutableGameState gameState = new ImmutableGameState(new Integer[]{10, 12},
                new BoardPeek(BoardFixture.createTestBoard()),
                Team.ONE);

        Rating expected = Rating.TWO.negate();
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }
}