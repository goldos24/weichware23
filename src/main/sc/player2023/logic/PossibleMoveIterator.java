package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.api.plugins.Vector;
import sc.plugin2023.Move;

import java.util.Iterator;

@SuppressWarnings("unused")
public class PossibleMoveIterator implements Iterator<Move> {
    BoardPeek board;
    ITeam team;

    private static final int halfTileXChange = 1;
    private static final int fullTileXChange = 2;
    private static final int fullTileYChange = 1;

    private static final Vector[] possibleMoveDirections = {
            new Vector(-fullTileXChange, 0), // left
            new Vector(-halfTileXChange, fullTileYChange), // top left
            new Vector(halfTileXChange, fullTileYChange), // top right
            new Vector(fullTileXChange, 0), // right
            new Vector(halfTileXChange, -fullTileYChange), // bottom right
            new Vector(-halfTileXChange, -fullTileYChange), // bottom left
    };

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
