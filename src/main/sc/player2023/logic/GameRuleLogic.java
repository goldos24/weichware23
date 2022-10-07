package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(ImmutableGameState gameState, Move move) {
        return gameState.withMove(move);
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return gameState.gameState().getPossibleMoves();
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        return RatingUtil.isTeamWinnerAfterGameEnd(gameState.gameState(), team);
    }
}
