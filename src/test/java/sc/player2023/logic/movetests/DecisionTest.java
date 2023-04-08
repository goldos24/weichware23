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
}
