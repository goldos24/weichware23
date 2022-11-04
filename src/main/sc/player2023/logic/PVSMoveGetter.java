package sc.player2023.logic;

import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;

public class PVSMoveGetter implements MoveGetter {
    private Rating pvs(@Nonnull ImmutableGameState gameState, int depth, double alpha, double beta,
                       @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer) {
        Iterable<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                Rating negated = pvs(childGameState, depth - 1, -beta, -alpha, rater, timeMeasurer).negate();
                score = negated.rating();
            }
            else {
                Rating negated = pvs(childGameState, depth - 1, -alpha - 1, -alpha, rater, timeMeasurer).negate();
                score = negated.rating(); /* * search with a null window */
                if (alpha < score && score < beta) {
                    Rating otherNegated = pvs(childGameState, depth - 1, -beta, -score, rater, timeMeasurer).negate();
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break; /* beta cut-off */
            }
        }
        return new Rating(alpha);
    }

    public PVSMoveGetter() {
        this.depth = 1;
    }

    public PVSMoveGetter(int depth) {
        this.depth = depth;
    }

    private final int depth;

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Rating highestRating = Rating.NEGATIVE_INFINITY;
        Move bestMove = null;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            Rating currentRating = pvs(childGameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, rater, timeMeasurer)
                    .negate();
            if (currentRating.isGreaterThan(highestRating)) {
                highestRating = currentRating;
                bestMove = move;
            }
        }
        if (bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }

}
