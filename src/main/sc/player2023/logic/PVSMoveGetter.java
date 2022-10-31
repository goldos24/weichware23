package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PVSMoveGetter implements MoveGetter {
    private Rating pvs(@Nonnull ImmutableGameState gameState, int depth, double alpha, double beta,
                       @Nonnull Rater rater) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || possibleMoves.isEmpty() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = GameRuleLogic.withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                Rating negated = pvs(childGameState, depth - 1, -beta, -alpha, rater).negate();
                score = negated.rating();
            }
            else {
                Rating negated = pvs(childGameState, depth - 1, -alpha - 1, -alpha, rater).negate();
                score = negated.rating(); /* * search with a null window */
                if (alpha < score && score < beta) {
                    Rating otherNegated = pvs(childGameState, depth - 1, -beta, -score, rater).negate();
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
        this.timeMeasurer = new TimeMeasurer(Logic.MAX_TIME);
    }

    public PVSMoveGetter(int depth, TimeMeasurer timeMeasurer) {
        this.depth = depth;
        this.timeMeasurer = timeMeasurer;
    }

    private final int depth;
    private final TimeMeasurer timeMeasurer;

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team, @Nonnull Rater rater) {
        timeMeasurer.reset();
        Rating highestRating = Rating.NEGATIVE_INFINITY;
        Move bestMove = null;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = GameRuleLogic.withMovePerformed(gameState, move);
            Rating currentRating = pvs(childGameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, rater).negate();
            if (currentRating.isGreaterThan(highestRating)) {
                highestRating = currentRating;
                bestMove = move;
            }
        }
        if(bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }
}
