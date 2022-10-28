package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class ImmutableGameState {
    @Nonnull
    private final GameState gameState;
    @Nonnull
    private final ImmutableMap<ITeam, Integer> teamPointsMap;

    public BoardPeek getBoard() {
        return board;
    }

    private final BoardPeek board;

    public ImmutableGameState(@Nonnull GameState gameState, @Nonnull ImmutableMap<ITeam, Integer> teamPointsMap) {
        this.gameState = gameState.clone();
        this.teamPointsMap = teamPointsMap;
        this.board = new BoardPeek(this.gameState.getBoard());
    }

    public int getPointsForTeam(ITeam team) {
        var result = teamPointsMap.get(team);
        assert result != null;
        return result;
    }

    public GameState getGameState() {
        return gameState.clone();
    }

    public boolean isOver() {
        return gameState.isOver();
    }

    public boolean stillRunning() {
        return !isOver();
    }

    @Nonnull
    public ImmutableMap<ITeam, Integer> getTeamPointsMap() {
        return teamPointsMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ImmutableGameState) obj;
        return Objects.equals(this.gameState, that.gameState) &&
                Objects.equals(this.teamPointsMap, that.teamPointsMap) &&
                Objects.equals(this.board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState, teamPointsMap, board);
    }

    @Override
    public String toString() {
        return "ImmutableGameState[" +
                "gameState=" + gameState + ", " +
                "teamPointsMap=" + teamPointsMap + ", " +
                "board=" + board + ']';
    }

}
