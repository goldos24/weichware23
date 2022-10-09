package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(ImmutableGameState gameState, Move move) {
        GameState realGameState = gameState.gameState();
        ITeam team = realGameState.getCurrentTeam();
        var teamPointsMap = gameState.teamPointsMap();
        var targetField = realGameState.getBoard().get(move.getTo());
        System.out.println(targetField.getFish());
        Integer ownPoints = teamPointsMap.get(team);
        Integer opponentPoints = teamPointsMap.get(team.opponent());
        assert ownPoints != null;
        assert opponentPoints != null;
        teamPointsMap = ImmutableMap.<ITeam, Integer>builder().
                put(team.opponent(), opponentPoints).
                put(team, ownPoints + targetField.getFish()).
                build();
        realGameState.performMove(move);
        System.out.println(teamPointsMap);
        return new ImmutableGameState(realGameState, teamPointsMap);
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return gameState.gameState().getPossibleMoves();
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        return RatingUtil.isTeamWinnerAfterGameEnd(gameState.gameState(), team);
    }
}
