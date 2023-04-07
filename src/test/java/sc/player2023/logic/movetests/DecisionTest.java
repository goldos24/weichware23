package sc.player2023.logic.movetests;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecisionTest {
    @Test
    void twoFishAndCutOffOrThreeFishTest() {
        BoardPeek testBoard = BoardParser.boardFromString("""
                -   G G G   - -\s
                 -   P P P     -\s
                - -         P  \s
                 - - - -   =   -\s
                - - - -   G   -\s
                 - - - -   -   -\s
                - - - -   =   -\s
                 - - - -     - -\s
                """);
        Team team = Team.ONE;
        GameScore score = new GameScore(0, 0);
        FailSoftPVSMoveGetter moveGetter = new FailSoftPVSMoveGetter();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        ImmutableGameState gameState = new ImmutableGameState(score, testBoard, team);
        Rater rater = Logic.createCombinedRater();
        Move move = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        assertEquals(new Move(new Coordinates(10, 4), new Coordinates(11, 5)), move);
    }

    @Test
    void twoFishAndCutOffOrThreeFishOrSomethingElseTest() {
        BoardPeek testBoard = BoardParser.boardFromString("""
                3 3 G G G   - -\s
                 3   P P P     -\s
                3 3         P  \s
                 3 3 3 3   =   -\s
                3 3 3 3   G   -\s
                 3 3 3 4   -   -\s
                4 4 4 4   =   -\s
                 4 4 4 4     - -\s
                """);
        Team team = Team.ONE;
        GameScore score = new GameScore(0, 0);
        FailSoftPVSMoveGetter moveGetter = new FailSoftPVSMoveGetter();
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        ImmutableGameState gameState = new ImmutableGameState(score, testBoard, team);
        Rater rater = Logic.createNewCombinedRater();
        var transFactory = new SimpleTransPositionTableFactory();
        int depth = 3;
        Move move = moveGetter.getBestMoveForDepth(gameState, rater, timeMeasurer, depth, transFactory.createTransPositionTableFromDepth(depth));
        assertEquals(new Move(new Coordinates(10, 4), new Coordinates(11, 5)), move);
    }
}
