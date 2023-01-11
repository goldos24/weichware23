package sc.player2023.logic.pvs;

import org.jetbrains.annotations.NotNull;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

import static sc.player2023.logic.pvs.AlphaBetaTranspositionTableUtil.addExactIfNotPresent;

public class NegaMax {
    public static RatedMove negaMax(@Nonnull ImmutableGameState gameState, int depth,
                                    @Nonnull SearchWindow searchWindow, @Nonnull ConstantPVSParameters constParams) {
        final TransPositionTable transPositionTable = constParams.transPositionTable();
        if(transPositionTable.hasGameStateWithExactRating(gameState)) { // transposition table cutoff
            return new RatedMove(transPositionTable.getRatingForGameState(gameState), null);
        }
        if(hasToCancelSearch(gameState, depth, constParams)) {
            Rating rating = constParams.rater().rate(gameState);
            addExactIfNotPresent(gameState, transPositionTable, rating);
            return new RatedMove(rating, null);
        }
        Iterable<Move> possibleMoves = constParams.moveGenerator().getPossibleMoves(gameState);
        Move bestMove = null;
        Rating bestRating = Rating.NEGATIVE_INFINITY;
        int postMoveRatingFactor = MoveGetterUtil.getRatingFactorForNextMove(gameState);
        for(Move currentMove : possibleMoves) {
            SearchWindow nextSearchWindow = new SearchWindow(-searchWindow.upperBound(), -bestRating.rating());
            ImmutableGameState nextGameState = GameRuleLogic.withMovePerformed(gameState, currentMove);
            Rating rawRating = negaMax(nextGameState, depth - 1, nextSearchWindow, constParams).rating();
            Rating currentRating = rawRating.multiply(
                    postMoveRatingFactor
            );
            if(bestMove == null || bestRating.isLessThan(currentRating)) {
                bestMove = currentMove;
                bestRating = currentRating;
            }
            if(bestRating.rating() >= searchWindow.upperBound()) {
                break;
            }
        }
        return new RatedMove(bestRating, bestMove);
    }

    private static boolean hasToCancelSearch(@NotNull ImmutableGameState gameState, int depth, @NotNull ConstantPVSParameters constParams) {
        return depth < 0 || constParams.timeMeasurer().ranOutOfTime() || !GameRuleLogic.anyPossibleMovesForPlayer(gameState.getBoard(), gameState.getCurrentTeam());
    }
}
