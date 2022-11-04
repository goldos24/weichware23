package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;

class PVSMoveGetterTest {
    @Test
    void yieldsAnyIfGivenNoTime() {
        Rater rater = new StupidRater();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningZeroTimeMeasurer();
        PVSMoveGetter moveGetter = new PVSMoveGetter(2);
        try {
            Thread.sleep(2);
        } catch (InterruptedException ignored) {}
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Move move = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        assertNotNull(move);
    }

    private static final Move bestMove = new Move(BoardFixture.firstPenguinCoords, new Coordinates(2, 0));

    @Test
    void findsBestMoveWithStupidDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 0;
        Rater rater = new StupidRater();
        PVSMoveGetter moveGetter = new PVSMoveGetter(depth);
        Move move = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        assertEquals(bestMove, move);
    }

    @Test
    void findsBestMoveWithHigherDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 1;
        Rater rater = new StupidRater();
        PVSMoveGetter moveGetter = new PVSMoveGetter(depth);
        Move move = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        assertEquals(bestMove, move);
    }

}