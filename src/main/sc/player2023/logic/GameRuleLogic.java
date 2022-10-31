package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(ImmutableGameState gameState, Move move) {
        GameState realGameState = gameState.getGameState();
        ITeam team = realGameState.getCurrentTeam();
        var teamPointsMap = gameState.getTeamPointsMap();
        var targetField = realGameState.getBoard().get(move.getTo());
        Integer ownPoints = teamPointsMap.get(team);
        Integer opponentPoints = teamPointsMap.get(team.opponent());
        assert ownPoints != null;
        assert opponentPoints != null;
        teamPointsMap = ImmutableMap.<ITeam, Integer>builder().
                put(team.opponent(), opponentPoints).
                put(team, ownPoints + targetField.getFish()).
                build();
        realGameState.performMove(move);
        return new ImmutableGameState(realGameState, teamPointsMap);
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return gameState.getGameState().getPossibleMoves();
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        return RatingUtil.isTeamWinnerAfterGameEnd(gameState.getGameState(), team);
    }

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    public static final int RIGHTMOST_X = BOARD_WIDTH*2-1;

    public static boolean coordsValid(Coordinates coordinates) {
        if(coordinates == null)
            return false;
        int x = coordinates.getX();
        int y = coordinates.getY();
        return x >= 0 && x <= RIGHTMOST_X && y >= 0 && y < BOARD_HEIGHT && x % 2 == y % 2;
    }

    public static boolean canMoveTo(@Nonnull BoardPeek board, @Nonnull Coordinates target) {
        return coordsValid(target) && board.get(target).getFish() != 0;
    }
}
