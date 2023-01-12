package sc.player2023.logic.pvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.StupidMoveGetter;
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

import javax.annotation.Nonnull;

import java.util.Arrays;

import static sc.player2023.logic.pvs.FailSoftPVSMoveGetter.pvs;
import static sc.player2023.logic.rating.RatingUtil.isInSearchWindow;

/**
 * @author Till Fransson
 * @since 22.12.2022
 */
public class AspiredPVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(AspiredPVSMoveGetter.class);

    private static final StupidMoveGetter GUESS_CREATION_MOVE_GETTER = new StupidMoveGetter();

    private static Rating createAspirationGuess(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer, int depth) {
        for(int moveCount = 0; moveCount < depth && GameRuleLogic.anyPossibleMovesForPlayer(gameState.getBoard(), gameState.getCurrentTeam()); ++moveCount) {
            Move move = GUESS_CREATION_MOVE_GETTER.getBestMove(gameState, rater, timeMeasurer);
            gameState = GameRuleLogic.withMovePerformed(gameState, move);
        }
        return rater.rate(gameState);
    }

    public AspiredPVSMoveGetter() {

    }

    TransPositionTableFactory transPositionTableFactory = new SimpleTransPositionTableFactory();

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);
        int depth = 0;
        Rating[] lastRating = new Rating[2];
        Arrays.fill(lastRating, rater.rate(gameState));
        while (!timeMeasurer.ranOutOfTime()) {
            TransPositionTable transPositionTable = transPositionTableFactory.createTransPositionTableFromDepth(depth);
            RatedMove currentRatedMove = getBestMoveForDepth(gameState, rater, timeMeasurer, depth, transPositionTable,
                                                             lastRating[depth % 2]);
            lastRating[depth % 2] = currentRatedMove.rating();
            Move currentMove = currentRatedMove.move();
            if (currentMove == null) {
                continue;
            }
            if (timeMeasurer.ranOutOfTime() && depth != 0) {
                break;
            }
            if (depth > GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT) {
                break;
            }
            bestMove = currentMove;
            depth++;
        }
        log.info("PVS Depth : {}", depth - 1);
        log.info("Board: {}", gameState.getBoard());
        return bestMove;
    }

    @Nonnull
    public static RatedMove getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                                @Nonnull TimeMeasurer timeMeasurer, int depth,
                                                @Nonnull TransPositionTable transPositionTable,
                                                @Nonnull Rating lastRating) {
        final int initialOffset = 12;
        int wideningFactor = 3;
        int offsetUpperBound = initialOffset;
        int offsetLowerBound = -initialOffset;
        int lowerBound = lastRating.add(offsetLowerBound).rating();
        int upperBound = lastRating.add(offsetUpperBound).rating();
        do {
            SearchWindow searchWindow = new SearchWindow(lowerBound, upperBound);
            RatedMove currentMove = pvs(gameState, depth, searchWindow, rater, timeMeasurer, transPositionTable,
                                        PossibleMoveIterable::new);
            Rating currentRating = currentMove.rating();
            boolean inSearchWindow = isInSearchWindow(searchWindow, currentRating);
            if (inSearchWindow) {
                log.info("lastRating: {}", lastRating);
                log.info("currentRating : {}", currentRating);
                log.info("SearchWindow: {}", searchWindow);
                return currentMove;
            }
            double rating = currentRating.rating();
            if (rating <= lowerBound) {
                upperBound = lowerBound;
                offsetLowerBound *= wideningFactor;
                lowerBound = lastRating.add(offsetLowerBound).rating();
            }
            else {
                lowerBound = upperBound;
                offsetUpperBound *= wideningFactor;
                upperBound = lastRating.add(offsetUpperBound).rating();
            }
        }
        while (!timeMeasurer.ranOutOfTime());
        return new RatedMove(Rating.NEGATIVE_INFINITY, null);
    }

}
