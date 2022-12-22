package sc.player2023.logic.movetests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.AlphaBeta;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.RatingWithMove;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

public class OpponentCutOffTest {
    TransPositionTableFactory transPositionTableFactory = new SimpleTransPositionTableFactory();


    String LATE_GAME_HUGE_CUTOFF_SITUATION = """
                4 = G 3 P     -\s
                       3   = -  \s
                  = G -   -   =\s
                 G - = =     - =\s
                P -     = =   =\s
                     P     =   P\s
                               \s
                 -       3 4 G  \s
                """;
    Move LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE = new Move(new Coordinates(8, 0), new Coordinates(5, 3));

    int LATE_GAME_HUGE_CUTOFF_DEPTH = 5;

    @Test
    void lateGameHugeCutOff() {
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        Move got = FailSoftPVSMoveGetter.getBestMoveForDepth(gameState, rater, TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(), LATE_GAME_HUGE_CUTOFF_DEPTH,
                transPositionTableFactory.createTransPositionTableFromDepth(5));
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }
    @Test
    void reducedWindowLateGameHugeCutOff() {
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        AlphaBeta alphaBeta = new AlphaBeta(-210, -208);
        RatingWithMove ratingWithMove = FailSoftPVSMoveGetter.pvs(gameState, LATE_GAME_HUGE_CUTOFF_DEPTH, alphaBeta, rater,
                TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                transPositionTableFactory.createTransPositionTableFromDepth(LATE_GAME_HUGE_CUTOFF_DEPTH));
        Move got = ratingWithMove.move();
        System.out.println(ratingWithMove.rating());
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }
}
