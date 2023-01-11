package sc.player2023.logic.pvs;

import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;
import static sc.player2023.logic.pvs.MoveGetterUtil.getRatingFactorForNextMove;
import static sc.player2023.logic.rating.RatingUtil.isInSearchWindow;

public class PrincipalVariationSearch {
    public static RatedMove pvs(@Nonnull ImmutableGameState gameState, int depth, SearchWindow searchWindow,
                                @Nonnull ConstantPVSParameters constParams) {
        final TransPositionTable transPositionTable = constParams.transPositionTable();
        final PossibleMoveGenerator moveGenerator = constParams.moveGenerator();
        final Rater rater = constParams.rater();
        final TimeMeasurer timeMeasurer = constParams.timeMeasurer();
        if (transPositionTable.hasGameStateWithExactRating(gameState)) {
            return new RatedMove(transPositionTable.getRatingForGameState(gameState), null);
        }
        Iterable<Move> possibleMoves = moveGenerator.getPossibleMoves(gameState);
        boolean isEmpty = !possibleMoves.iterator().hasNext();
        if (depth < 0 || gameState.isOver() || isEmpty || timeMeasurer.ranOutOfTime()) {
            Rating rating = rater.rate(gameState);
            transPositionTable.addExact(gameState, rating);
            return new RatedMove(rating, null);
        }
        boolean firstChild = true;
        Move bestMove = null;
        int score, bestScore = Integer.MIN_VALUE;
        int lowerBound = searchWindow.lowerBound();
        int upperBound = searchWindow.upperBound();
        int postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                SearchWindow newSearchWindow = new SearchWindow(-upperBound, -lowerBound);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, constParams)
                        .rating().multiply(
                                postMoveRatingFactor
                        );
                score = negated.rating();
                bestScore = score;
                bestMove = move;
                if (score > lowerBound) {
                    if (score >= upperBound) {
                        break;
                    }
                    lowerBound = score;
                }
            }
            else {
                SearchWindow newSearchWindow = new SearchWindow(-lowerBound - 1, -lowerBound);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, constParams).
                        rating().multiply(
                                postMoveRatingFactor
                        );
                score = negated.rating(); /* * search with a null window */
                if (score > lowerBound && score < upperBound) {
                    SearchWindow newSearchWindowCut = new SearchWindow(-upperBound, -lowerBound);
                    Rating otherNegated = pvs(childGameState, depth - 1, newSearchWindowCut, constParams).
                            rating().multiply(
                                    postMoveRatingFactor
                            );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                    if (score > lowerBound) {
                        lowerBound = score;
                    }
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                    if (score >= upperBound) {
                        break; // Beta cut-off
                    }
                }
            }
        }
        Rating rating = new Rating(bestScore);
        if(isInSearchWindow(new SearchWindow(lowerBound, upperBound), rating)) {
            transPositionTable.addExact(gameState, rating);
        }
        return new RatedMove(rating, bestMove);
    }

}
