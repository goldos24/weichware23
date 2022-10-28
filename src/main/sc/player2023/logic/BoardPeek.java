package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;

import java.util.Collection;

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
}
