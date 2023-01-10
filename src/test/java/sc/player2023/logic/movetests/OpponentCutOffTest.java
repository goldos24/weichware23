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
import sc.player2023.logic.pvs.ConstantPVSParameters;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.pvs.PrincipalVariationSearch;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;
import static sc.player2023.logic.MoveGetters.FAIL_SOFT_PVS_MOVE_GETTER;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpponentCutOffTest {
    private static final TimeMeasurer ALREADY_RUNNING_INFINITE_TIME_MEASURER = TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer();
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

    private final static int LATE_GAME_HUGE_CUTOFF_DEPTH = 5;
    private static final Rater RATER = Logic.createCombinedRater();

    @Test
    void lateGameHugeCutOffFailSoft() {
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        Move got = FAIL_SOFT_PVS_MOVE_GETTER.getBestMoveForDepth(gameState, RATER,
                                                             ALREADY_RUNNING_INFINITE_TIME_MEASURER,
                                                             LATE_GAME_HUGE_CUTOFF_DEPTH,
                                                             transPositionTableFactory.createTransPositionTableFromDepth(
                                                                     5));
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }

    @Test
    void lateGameHugeCutOffAspiration() {
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        Rating lastRating = Rating.ZERO;
        TransPositionTable table = transPositionTableFactory.createTransPositionTableFromDepth(5);
        RatedMove got = AspiredPVSMoveGetter.getBestMoveForDepth(gameState, RATER, ALREADY_RUNNING_INFINITE_TIME_MEASURER,
                                                                 LATE_GAME_HUGE_CUTOFF_DEPTH, table, lastRating);
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got.move());
    }

    @Test
    void reducedWindowLateGameHugeCutOff() {
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(
                LATE_GAME_HUGE_CUTOFF_SITUATION), Team.TWO);
        SearchWindow searchWindow = new SearchWindow(-235, -233);
        RatedMove ratedMove = PrincipalVariationSearch.pvs(gameState, LATE_GAME_HUGE_CUTOFF_DEPTH, searchWindow,
                new ConstantPVSParameters(
                RATER,
                ALREADY_RUNNING_INFINITE_TIME_MEASURER,
                transPositionTableFactory.createTransPositionTableFromDepth(LATE_GAME_HUGE_CUTOFF_DEPTH),
                FailSoftPVSMoveGetter::getShuffledPossibleMoves));
        Move got = ratedMove.move();
        System.out.println(ratedMove.rating());
        assertEquals(LATE_GAME_HUGE_CUTOFF_EXPECTED_MOVE, got);
    }

    private static final String EARLY_GAME_CUTOFF_SITUATION = """
            = - 4 = 3 -   3\s
             = - P 3 G = = -\s
              =   G = = = =\s
             -     = - P -  \s
              - -   =     -\s
             = = = =     =  \s
            - = P G G P - =\s
             3   - 3 = 4 - =\s
            """;

    private static final ImmutableGameState EARLY_GAME_CUTOFF_GAME_STATE = new ImmutableGameState(
            new GameScore(6, 6),
            BoardParser.boardFromString(EARLY_GAME_CUTOFF_SITUATION),
            Team.ONE
    );

    private static final int EARLY_GAME_CUTOFF_DEPTH = 3;

    @Test
    void earlyGameCutOffTest() {
        Move expected = new Move(new Coordinates(6, 6), new Coordinates(5, 5));
        RatedMove move = PrincipalVariationSearch.pvs(
                EARLY_GAME_CUTOFF_GAME_STATE,
                EARLY_GAME_CUTOFF_DEPTH, new SearchWindow(Rating.PRIMITIVE_LOWER_BOUND, Rating.PRIMITIVE_UPPER_BOUND),
                new ConstantPVSParameters(
                    RATER, ALREADY_RUNNING_INFINITE_TIME_MEASURER,
                    transPositionTableFactory.createTransPositionTableFromDepth(EARLY_GAME_CUTOFF_DEPTH), FailSoftPVSMoveGetter::getShuffledPossibleMoves
                )
        );
        System.out.println(move.rating());
        assertEquals(move.move(), expected);
    }
}
