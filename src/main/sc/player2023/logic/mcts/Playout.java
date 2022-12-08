package sc.player2023.logic.mcts;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class Playout {

    public static ImmutableGameState completeRandomly(@Nonnull ImmutableGameState initialGameState) {
        ImmutableGameState currentGameState = initialGameState;
        while (!currentGameState.isOver()) {
            currentGameState = GameRuleLogic.withRandomMovePerformed(currentGameState);
        }
        return currentGameState;
    }

    public static ImmutableGameState completeByRater(@Nonnull ImmutableGameState initialGameState, @Nonnull Rater rater) {
        ImmutableGameState currentGameState = initialGameState;
        while (!currentGameState.isOver()) {
            Rating bestRating = Rating.NEGATIVE_INFINITY;
            Move bestMove = null;
            List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(currentGameState);

            for (Move move : possibleMoves) {
                ImmutableGameState newGameState = GameRuleLogic.withMovePerformed(currentGameState, move);
                Rating rating = rater.rate(newGameState);

                if (rating.isGreaterThan(bestRating)) {
                    bestRating = rating;
                    bestMove = move;
                }
            }

            assert bestMove != null;
            currentGameState = GameRuleLogic.withMovePerformed(currentGameState, bestMove);
        }
        return currentGameState;
    }
}
