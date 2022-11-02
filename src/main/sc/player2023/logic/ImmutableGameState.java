package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class ImmutableGameState {
    @Nonnull
    private final ImmutableMap<ITeam, Integer> teamPointsMap;

    public BoardPeek getBoard() {
        return board;
    }

    private final BoardPeek board;

    public ImmutableGameState(@Nonnull GameState gameState, @Nonnull ImmutableMap<ITeam, Integer> teamPointsMap) {
        this.teamPointsMap = teamPointsMap;
        this.board = new BoardPeek(gameState.getBoard());
        this.supposedCurrentTeam = gameState.getCurrentTeam();
    }

    public ImmutableGameState(@Nonnull ImmutableMap<ITeam, Integer> teamPointsMap, BoardPeek board, ITeam supposedCurrentTeam) {
        this.teamPointsMap = teamPointsMap;
        this.board = board;
        this.supposedCurrentTeam = supposedCurrentTeam;
    }

    private final ITeam supposedCurrentTeam;

    public ITeam getCurrentTeam() {
        if(GameRuleLogic.anyPossibleMovesForPlayer(board, supposedCurrentTeam))
            return supposedCurrentTeam;
        return supposedCurrentTeam.opponent();
    }

    public int getPointsForTeam(ITeam team) {
        var result = teamPointsMap.get(team);
        assert result != null;
        return result;
    }

    public boolean isOver() {
        return !(GameRuleLogic.anyPossibleMovesForPlayer(board, getCurrentTeam()));
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
        return Objects.equals(this.getCurrentTeam(), that.getCurrentTeam()) &&
                Objects.equals(this.teamPointsMap, that.teamPointsMap) &&
                Objects.equals(this.board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentTeam(), teamPointsMap, board);
    }

    @Override
    public String toString() {
        return "ImmutableGameState[" +
                "team=" + getCurrentTeam() + ", " +
                "teamPointsMap=" + teamPointsMap + ", " +
                "board=" + board + ']';
    }

}
