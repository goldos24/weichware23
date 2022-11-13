package sc.player2023.logic.gameState;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.Objects;

@Immutable
public final class ImmutableGameState {
    @Nonnull
    private final Integer[] teamPointsMap;

    public BoardPeek getBoard() {
        return board;
    }

    private final BoardPeek board;

    public ImmutableGameState(@Nonnull GameState gameState, @Nonnull Integer[] teamPointsMap) {
        this.teamPointsMap = teamPointsMap;
        this.board = new BoardPeek(gameState.getBoard());
        this.supposedCurrentTeam = gameState.getCurrentTeam();
    }

    public ImmutableGameState(@Nonnull Integer[] teamPointsMap, BoardPeek board, ITeam supposedCurrentTeam) {
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
        return teamPointsMap[((Team)team).ordinal()];
    }

    public boolean isOver() {
        return !(GameRuleLogic.anyPossibleMovesForPlayer(board, getCurrentTeam()));
    }

    public boolean stillRunning() {
        return !isOver();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ImmutableGameState) obj;
        return Objects.equals(this.getCurrentTeam(), that.getCurrentTeam()) &&
                Arrays.equals(this.teamPointsMap, that.teamPointsMap) &&
                Objects.equals(this.board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentTeam(), Arrays.hashCode(teamPointsMap), board);
    }

    @Override
    public String toString() {
        return "ImmutableGameState[" +
                "team=" + getCurrentTeam() + ", " +
                "teamPointsMap=" + Arrays.toString(teamPointsMap) + ", " +
                "board=" + board + ']';
    }

}
