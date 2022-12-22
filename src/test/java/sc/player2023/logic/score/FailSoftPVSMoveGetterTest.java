package sc.player2023.logic.score;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.StupidRater;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FailSoftPVSMoveGetterTest {

    private final TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();
    private final TransPositionTable table = transPositionTableFactory.createTransPositionTableFromDepth(5);

    @Test
    void yieldsAnyIfGivenNoTime() {
        Rater rater = new StupidRater();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningZeroTimeMeasurer();
        FailSoftPVSMoveGetter moveGetter = new FailSoftPVSMoveGetter();
        try {
            Thread.sleep(2);
        }
        catch (InterruptedException ignored) {
        }
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Move move = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        assertNotNull(move);
    }

    private static final Move bestMove = new Move(BoardFixture.FIRST_PENGUIN_COORDINATES, new Coordinates(2, 0));

    @Test
    void findsBestMoveWithStupidDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 0;
        Rater rater = new StupidRater();
        Move move = FailSoftPVSMoveGetter.getBestMoveForDepth(gameState, rater, timeMeasurer, depth, table);
        assertEquals(bestMove, move);
    }

    @Test
    void findsBestMoveWithHigherDepth() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        int depth = 1;
        Rater rater = new StupidRater();
        Move move = FailSoftPVSMoveGetter.getBestMoveForDepth(gameState, rater, timeMeasurer, depth, table);
        assertEquals(bestMove, move);
    }

}
