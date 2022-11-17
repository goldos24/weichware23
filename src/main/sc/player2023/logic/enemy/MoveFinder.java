package sc.player2023.logic.enemy;

import org.jetbrains.annotations.NotNull;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class MoveFinder implements MoveGetter {
    @Nonnull
    static Rating rateGameState(@Nonnull ImmutableGameState gameState, @Nonnull Rater gameStateRater, int depth, @Nonnull TimeMeasurer timer) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (depth != 0 && !possibleMoves.isEmpty()) {
            Rating max = Rating.NEGATIVE_INFINITY;
            Iterator<Move> var7 = possibleMoves.iterator();

            do {
                if (!var7.hasNext()) {
                    return max;
                }

                Move move = var7.next();
                ImmutableGameState withMovePerformed = GameRuleLogic.withMovePerformed(gameState, move);
                Rating rating = rateGameState(withMovePerformed, gameStateRater, depth - 1, timer).negate();
                if (rating.compareTo(max) > 0) {
                    max = rating;
                }
            } while(!timer.ranOutOfTime());

            return max;
        } else {
            return gameStateRater.rate(gameState);
        }
    }

    @Nonnull
    public Move findBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater gameStateRater, int depth, @Nonnull TimeMeasurer timer) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        Rating max = Rating.NEGATIVE_INFINITY;
        Move bestMove = possibleMoves.get(0);
        Iterator<Move> var8 = possibleMoves.iterator();

        do {
            if (!var8.hasNext()) {
                return bestMove;
            }

            Move move = var8.next();
            ImmutableGameState withMovePerformed = GameRuleLogic.withMovePerformed(gameState, move);
            Rating rating = rateGameState(withMovePerformed, gameStateRater, depth - 1, timer).negate();
            if (rating.compareTo(max) > 0) {
                max = rating;
                bestMove = move;
            }
        } while(!timer.ranOutOfTime());

        return bestMove;
    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        return findBestMove(gameState, rater,3, timeMeasurer);
    }
}
