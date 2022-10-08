package sc.player2023.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;

class StupidMoveGetterTest {

    private ImmutableGameState testGameState;

    @BeforeEach
    void setUp() {
        testGameState = GameStateFixture.createTestGameState();

    }

    @Test
    void stupidMoveGetterTest() {
        System.out.println(testGameState.getPointsForTeam(Team.ONE));
        System.out.println(testGameState.getPointsForTeam(Team.TWO));
        MoveGetter moveGetter = new StupidMoveGetter();
        Rater rater = new StupidRater();
        Move move = moveGetter.getBestMove(testGameState, Team.ONE, rater);
        testGameState = GameRuleLogic.withMovePerformed(testGameState, move);
        assertEquals(GameStateFixture.DEFAULT_MORE_FISH_COUNT, testGameState.getPointsForTeam(Team.ONE));
    }

    @Test
    void stupidRaterTest() {
        Rater rater = new StupidRater();
        assertEquals(-GameStateFixture.DEFAULT_MORE_FISH_COUNT, rater.rate(testGameState));

    }
}
