package sc.player2023.logic.gameState;

import sc.api.plugins.Team;
import sc.player2023.logic.rating.RatingUtil;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public class ImmutableGameStateFactory {
    @Nonnull
    public static ImmutableGameState createAny() {
        GameState gameState = new GameState();
        return createFromGameState(gameState);
    }

    @Nonnull
    public static ImmutableGameState createFromGameState(@Nonnull GameState gameState) {
        Integer[] teamPointsMap = new Integer[2];
        for(Team team : Team.values()) {
            teamPointsMap[team.ordinal()] = RatingUtil.getCombinedPointsForTeam(gameState, team);
        }
        return new ImmutableGameState(gameState.clone(), teamPointsMap);
    }
}
