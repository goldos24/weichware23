package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public record ImmutableGameState(@Nonnull GameState gameState, @Nonnull ImmutableMap<ITeam, Integer> teamPointsMap) {
    public ImmutableGameState(@Nonnull GameState gameState, @Nonnull ImmutableMap<ITeam, Integer> teamPointsMap) {
        this.gameState = gameState.clone();
        this.teamPointsMap = teamPointsMap;
    }
    public int getPointsForTeam(ITeam team) {
        var result = teamPointsMap.get(team);
        assert result != null;
        return result;
    }

    @Override
    public GameState gameState() {
        return gameState.clone();
    }

    public boolean isOver() {
        return gameState.isOver();
    }

    public boolean stillRunning() {
        return !isOver();
    }
}
