package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Objects;

@Immutable
public class BoardPeek {
    /*
     * TODO:
     *  complete reimplementation of board mapping it to integers
     *  rename to ImmutableBoard
     *  have similar methods to sc.plugin2023.Board, just immutable
     *  replace every usage of board in ratings et al. with BoardPeek / ImmutableBoard
     */

    private final Board board;

    public BoardPeek(Board board) {
        this.board = board;
    }

    public Collection<Pair<Coordinates, Team>> getPenguins() {
        return board.getPenguins();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardPeek boardPeek = (BoardPeek) o;
        return Objects.equals(board, boardPeek.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }

    @Override
    public String toString() {
        return "BoardPeek {" + board + "}";
    }
}
