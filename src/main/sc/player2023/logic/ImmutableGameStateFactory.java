package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
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
        var teamPointsMapBuilder = ImmutableMap.<ITeam, Integer>builder();
        for(ITeam team : Team.values()) {
            teamPointsMapBuilder.put(team, RatingUtil.getCombinedPointsForTeam(gameState, team));
        }
        var teamPointsMap = teamPointsMapBuilder.build();
        return new ImmutableGameState(gameState.clone(), teamPointsMap);
    }
}
