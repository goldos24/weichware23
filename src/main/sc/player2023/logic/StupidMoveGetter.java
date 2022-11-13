package sc.player2023.logic;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class StupidMoveGetter implements MoveGetter {
    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = null;
        Rating bestRating = Rating.NEGATIVE_INFINITY;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for (Move move : possibleMoves) {
            Rating ratedGameState = rater.rate(GameRuleLogic.withMovePerformed(gameState, move));
            Rating currentRating = ratedGameState.negate();
            if (currentRating.isGreaterThan(bestRating)) {
                bestRating = currentRating;
                bestMove = move;
            }
        }
        if(bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }
}
