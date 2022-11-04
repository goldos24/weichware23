package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;

class PVSMoveGetterTest {
    @Test
    void yieldsAnyIfGivenNoTime() {
        Rater rater = new StupidRater();
        PVSMoveGetter moveGetter = new PVSMoveGetter(2, TimeMeasurerFixture.createAlreadyRunningZeroTimeMeasurer());
        try {
            Thread.sleep(2);
        } catch (InterruptedException ignored) {}
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Move move = moveGetter.getBestMove(gameState, rater);
        assertNotNull(move);
    }

    private static final Move bestMove = new Move(BoardFixture.firstPenguinCoords, new Coordinates(2, 0));

    @Test
    void findsBestMoveWithStupidDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 0;
        Rater rater = new StupidRater();
        PVSMoveGetter moveGetter = new PVSMoveGetter(depth, timeMeasurer);
        Move move = moveGetter.getBestMove(gameState, rater);
        assertEquals(bestMove, move);
    }

    @Test
    void findsBestMoveWithHigherDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 1;
        Rater rater = new StupidRater();
        PVSMoveGetter moveGetter = new PVSMoveGetter(depth, timeMeasurer);
        Move move = moveGetter.getBestMove(gameState, rater);
        assertEquals(bestMove, move);
    }

}