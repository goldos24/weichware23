package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class StupidMoveGetter implements MoveGetter {
    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team, @Nonnull Rater rater) {
        Move bestMove = null;
        int bestRating = Integer.MIN_VALUE;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for(Move move : possibleMoves) {
            int currentRating = -rater.rate(GameRuleLogic.withMovePerformed(gameState, move));
            System.out.println(currentRating +" : "+ bestRating);
            if(currentRating > bestRating) {
                bestRating = currentRating;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
