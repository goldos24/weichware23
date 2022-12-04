package sc.player2023.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.StupidRater;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;

class StupidMoveGetterTest {

    private ImmutableGameState testGameState;

    @BeforeEach
    void setUp() {
        testGameState = GameStateFixture.createTestGameState();
        MoveGetter moveGetter = new StupidMoveGetter();
        Rater rater = new StupidRater();
        TimeMeasurer timeMeasurer = Logic.createDefaultRunningTimeMeasurer();
        Move move = moveGetter.getBestMove(testGameState, rater, timeMeasurer);
        testGameState = GameRuleLogic.withMovePerformed(testGameState, move);
    }

    @Test
    void stupidMoveGetterTest() {
        assertEquals(BoardFixture.DEFAULT_MORE_FISH_COUNT, testGameState.getPointsForTeam(Team.ONE));
    }

    @Test
    void stupidRaterTest() {
        Rater rater = new StupidRater();
        Rating expected = new Rating(-BoardFixture.DEFAULT_MORE_FISH_COUNT);
        assertEquals(expected, rater.rate(testGameState));

    }
}
