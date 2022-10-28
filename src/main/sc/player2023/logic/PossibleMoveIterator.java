package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.Move;

import java.util.Iterator;

@SuppressWarnings("unused")
public class PossibleMoveIterator implements Iterator<Move> {
    BoardPeek board;
    ITeam team;

    public PossibleMoveIterator(BoardPeek board, ITeam team) {
        this.board = board;
        this.team = team;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Move next() {
        return null;
    }
}
