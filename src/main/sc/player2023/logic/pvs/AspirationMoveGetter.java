package sc.player2023.logic.pvs;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.FishCountPreSorting;
import sc.player2023.logic.move.PossibleMoveIterable;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static sc.player2023.logic.pvs.PrincipalVariationSearch.pvs;
import static sc.player2023.logic.rating.RatingUtil.isInSearchWindow;

public interface AspirationMoveGetter extends IterativeDeepeningAlphaBetaMoveGetter {
    @Override
    default Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);
        int depth = 0;
        Rating[] lastRating = new Rating[2];
        Arrays.fill(lastRating, rater.rate(gameState));
        while (!timeMeasurer.ranOutOfTime()) {
            TransPositionTable transPositionTable = transPositionTableFactory().createTransPositionTableFromDepth(depth);
            RatedMove currentRatedMove = getBestMoveForDepth(gameState, rater, timeMeasurer, depth, transPositionTable,
                    lastRating[depth%2]);
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
        log().info("PVS Depth : {}", depth - 1);
        log().info("Board: {}", gameState.getBoard());
        return bestMove;
    }

    @Nonnull
    default RatedMove getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                                TimeMeasurer timeMeasurer, int depth,
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
            RatedMove currentMove = pvs(gameState, depth, searchWindow,
                    new ConstantPVSParameters(rater, timeMeasurer, transPositionTable,
                            PossibleMoveIterable::new));
            Rating currentRating = currentMove.rating();
            boolean inSearchWindow = isInSearchWindow(searchWindow, currentRating);
            if (inSearchWindow) {
                log().info("lastRating: {}", lastRating);
                log().info("currentRating : {}", currentRating);
                log().info("SearchWindow: {}", searchWindow);
                return currentMove;
            }
            double rating = currentRating.rating();
            if (rating <= lowerBound) {
                upperBound = lowerBound;
                offsetLowerBound *= wideningFactor;
                lowerBound = lastRating.add(offsetLowerBound).rating();
                continue;
            }
            lowerBound = upperBound;
            offsetUpperBound *= wideningFactor;
            upperBound = lastRating.add(offsetUpperBound).rating();
        }
        while (!timeMeasurer.ranOutOfTime());
        return new RatedMove(Rating.NEGATIVE_INFINITY, null);
    }
}
