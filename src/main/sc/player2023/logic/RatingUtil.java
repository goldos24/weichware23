package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import java.util.Map;
import static java.util.Map.entry;

public class RatingUtil {
    private static final Map<ITeam, Integer> teamIndexMap = Map.ofEntries(
            entry(Team.ONE, 0),
            entry(Team.TWO, 1)
    );

    public static int getCombinedPointsForTeam(@Nonnull GameState gameState, ITeam team) {
        return gameState.getFishes()[teamIndexMap.get(team)];
    }

    public static boolean isTeamWinnerAfterGameEnd(GameState gameState, ITeam team) {
        return RatingUtil.getCombinedPointsForTeam(gameState, team) > RatingUtil.getCombinedPointsForTeam(gameState, team.opponent());
    }

    public static int prefixForCurrentTeam(@Nonnull GameState gameState) {
        return gameState.getCurrentTeam() == Team.ONE ? 1 : -1;
    }
}
