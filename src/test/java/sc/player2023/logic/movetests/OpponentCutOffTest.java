package sc.player2023.logic.movetests;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpponentCutOffTest {
    private final TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();


    private final static String LATE_GAME_HUGE_CUTOFF_SITUATION = """
            4 = G 3 P     -\s
                   3   = -  \s
              = G -   -   =\s
             G - = =     - =\s
            P -     = =   =\s
                 P     =   P\s
                           \s
             -       3 4 G  \s
            """;
    private final static Move LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE = new Move(new Coordinates(8, 0),
                                                                             new Coordinates(5, 3));

    private final static int LATE_GAME_HUGE_CUTOFF_DEPTH = 2;

    @Test
    void lateGameHugeCutOffFailSoft() {
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        Move got = FailSoftPVSMoveGetter.getBestMoveForDepth(gameState, rater,
                                                             TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                                                             LATE_GAME_HUGE_CUTOFF_DEPTH,
                                                             transPositionTableFactory.createTransPositionTableFromDepth(
                                                                     5));
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }

    @Test
    void lateGameHugeCutOffAspiration() {
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        Rating lastRating = Rating.ZERO;
        TransPositionTable table = transPositionTableFactory.createTransPositionTableFromDepth(5);
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
        RatedMove got = AspiredPVSMoveGetter.getBestMoveForDepth(gameState, rater, timeMeasurer,
                                                                 LATE_GAME_HUGE_CUTOFF_DEPTH, table, lastRating);
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got.move());
    }

    @Test
    void reducedWindowLateGameHugeCutOff() {
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        SearchWindow searchWindow = new SearchWindow(-210, -208);
        RatedMove ratedMove = FailSoftPVSMoveGetter.pvs(gameState, LATE_GAME_HUGE_CUTOFF_DEPTH, searchWindow, rater,
                                                        TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                                                        transPositionTableFactory.createTransPositionTableFromDepth(
                                                                LATE_GAME_HUGE_CUTOFF_DEPTH),
                                                        FailSoftPVSMoveGetter::getShuffledPossibleMoves);
        Move got = ratedMove.move();
        System.out.println(ratedMove.rating());
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }
}
