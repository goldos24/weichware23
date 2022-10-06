package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PVSMoveGetter implements MoveGetter {
    private int pvs(@Nonnull ImmutableGameState gameState, int depth, int alpha, int beta, @Nonnull Rater rater) {
        if(depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        int score;
        for(Move move : possibleMoves) {
            ImmutableGameState childGameState = GameRuleLogic.withMovePerformed(gameState, move);
            if(firstChild) {
                firstChild = false;
                score = -pvs(childGameState, depth - 1, -beta, -alpha, rater);
            } else {
            score = -pvs(childGameState, depth -1, -alpha-1, -alpha, rater); /* * search with a null window */
            if(alpha < score && score < beta)
                score = -pvs(childGameState, depth -1, -beta, -score, rater); /* if it failed high, do a full re-search */
            }
            alpha = Math.max(alpha, score);
            if(alpha >= beta)
                break; /* beta cut-off */
        }
        return alpha;
    }

    TimeMeasurer timeMeasurer = new TimeMeasurer(Logic.MAX_TIME);

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team, @Nonnull Rater rater) {
        timeMeasurer.reset();
        int highestRating = Integer.MIN_VALUE;
        Move bestMove = null;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for(Move move : possibleMoves) {
            ImmutableGameState childGameState = GameRuleLogic.withMovePerformed(gameState, move);
            int currentRating = -pvs(childGameState, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, rater);
            if(currentRating > highestRating) {
                bestMove = move;
            }
        }
        return bestMove;
    }
}
