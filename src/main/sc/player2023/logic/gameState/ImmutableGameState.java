package sc.player2023.logic.gameState;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.score.PlayerScore;
import sc.player2023.logic.score.Score;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class ImmutableGameState {
    @Nonnull
    private final GameScore gameScore;

    public BoardPeek getBoard() {
        return board;
    }

    private final BoardPeek board;

    public ImmutableGameState(@Nonnull GameState gameState, @Nonnull GameScore gameScore) {
        this.gameScore = gameScore;
        this.board = new BoardPeek(gameState.getBoard());
        this.supposedCurrentTeam = gameState.getCurrentTeam();
    }

    public ImmutableGameState(@Nonnull GameScore gameScore, BoardPeek board, ITeam supposedCurrentTeam) {
        this.gameScore = gameScore;
        this.board = board;
        this.supposedCurrentTeam = supposedCurrentTeam;
    }

    private final ITeam supposedCurrentTeam;

    public ITeam getCurrentTeam() {
        if (GameRuleLogic.anyPossibleMovesForPlayer(board, supposedCurrentTeam)) {
            return supposedCurrentTeam;
        }
        return supposedCurrentTeam.opponent();
    }

    public Score getScoreForTeam(ITeam team) {
        return gameScore.getScoreFromTeam(team);
    }

    public PlayerScore getPlayerScoreForTeam(ITeam team) {
        return gameScore.getPlayerScoreFromTeam(team);
    }

    public boolean isOver() {
        return !(GameRuleLogic.anyPossibleMovesForPlayer(board, getCurrentTeam()));
    }

    public boolean stillRunning() {
        return !isOver();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutableGameState)) {
            return false;
        }
        ImmutableGameState that = (ImmutableGameState) o;
        return gameScore.equals(that.gameScore) && Objects.equals(board, that.board) && Objects.equals(
                supposedCurrentTeam, that.supposedCurrentTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameScore, board, supposedCurrentTeam);
    }

    @Override
    public String toString() {
        return "ImmutableGameState {" +
                " gameScore = " + gameScore +
                ", board = " + board +
                ", supposedCurrentTeam = " + supposedCurrentTeam +
                "\n}";
    }

}
