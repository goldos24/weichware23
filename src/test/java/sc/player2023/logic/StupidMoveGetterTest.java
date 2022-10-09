package sc.player2023.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import sc.api.plugins.Team;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

class StupidMoveGetterTest {

    private static final Logger log = getLogger(StupidMoveGetterTest.class);


    private ImmutableGameState testGameState;

    @BeforeEach
    void setUp() {
        testGameState = GameStateFixture.createTestGameState();

    }

    @Test
    void stupidMoveGetterTest() {
        log.info("Points of Team One before Move: {}", testGameState.getPointsForTeam(Team.ONE));
        log.info("Points of Team Two before Move: {}", testGameState.getPointsForTeam(Team.TWO));
        MoveGetter moveGetter = new StupidMoveGetter();
        Rater rater = new StupidRater();
        Move move = moveGetter.getBestMove(testGameState, Team.ONE, rater);
        log.info("bestMove: {}", move);
        testGameState = GameRuleLogic.withMovePerformed(testGameState, move);
        assertEquals(BoardFixture.DEFAULT_MORE_FISH_COUNT, testGameState.getPointsForTeam(Team.ONE));
    }

    @Test
    void stupidRaterTest() {
        Rater rater = new StupidRater();
        assertEquals(-BoardFixture.DEFAULT_MORE_FISH_COUNT, rater.rate(testGameState));

    }
}
