package sc.player2023.logic.pvs;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveIterable;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import java.util.Arrays;

import static sc.player2023.logic.GameRuleLogic.getPossibleMoves;

public class MTDFMoveGetter implements MoveGetter {
    private static final Logger log = LoggerFactory.getLogger(MTDFMoveGetter.class);

    private final static TransPositionTableFactory transPositionTableFactory = new SimpleTransPositionTableFactory();

    @Override
    public Move getBestMove(@NotNull ImmutableGameState gameState, @NotNull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = getPossibleMoves(gameState).get(0);
        int depth = 2;
        RatedMove initialRatedMove = PrincipalVariationSearch.pvs(gameState, depth, new SearchWindow(Rating.PRIMITIVE_LOWER_BOUND, Rating.PRIMITIVE_UPPER_BOUND),
                new ConstantPVSParameters(rater, timeMeasurer, transPositionTableFactory.createTransPositionTableFromDepth(depth), PossibleMoveIterable::new));
        Rating firstGuess = initialRatedMove.rating();
        Rating[] lastRatedMove = new Rating[2];
        Arrays.fill(lastRatedMove, firstGuess);
        while (!timeMeasurer.ranOutOfTime()) {
            int lastRatingIndex = depth % 2;
            TransPositionTable transPositionTable = transPositionTableFactory.createTransPositionTableFromDepth(depth);
            int depthClone = depth;
            RatedMove currentMove = GameRuleLogic.getPossibleMoveStream(gameState).map(move -> new RatedMove(new Rating(getBestMoveForDepth(gameState, rater, timeMeasurer, depthClone, transPositionTable, lastRatedMove[lastRatingIndex])), move)).reduce((ratedMove, ratedMove2) -> ratedMove.rating().isGreaterThan(ratedMove2.rating()) ? ratedMove : ratedMove2).orElse(null);
            if (currentMove == null) continue;
            lastRatedMove[lastRatingIndex] = currentMove.rating();
            if (depth > GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT) {
                break;
            }
            bestMove = currentMove.move();
            depth++;
        }
        log.info("PVS Depth : {}", depth - 1);
        log.info("Board: {}", gameState.getBoard());
        return bestMove;
    }

    private static int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    private int getBestMoveForDepth(ImmutableGameState gameState, Rater rater, TimeMeasurer timeMeasurer, int depth, TransPositionTable transPositionTable, Rating firstGuess) {
        int f = firstGuess.rating();
        int[] bound = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}; // lower, upper
        do {
            int beta = f + boolToInt(f == bound[0]);
            RatedMove ratedMove = PrincipalVariationSearch.pvs(gameState, depth, new SearchWindow(beta - 1, beta),
                    new ConstantPVSParameters(rater, timeMeasurer, transPositionTable, PossibleMoveIterable::new));
            f = ratedMove.rating().rating();
            bound[boolToInt(f < beta)] = f;
        } while (bound[0] < bound[1]);
        return f;
    }
}
