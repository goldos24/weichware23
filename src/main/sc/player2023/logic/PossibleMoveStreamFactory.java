package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class PossibleMoveStreamFactory implements Iterator<Move> {
    BoardPeek board;
    ITeam team;

    private static final int halfTileXChange = 1;
    private static final int fullTileXChange = 2;
    private static final int fullTileYChange = 1;

    @Nonnull
    private static final Vector[] possibleMoveDirections = {
            new Vector(-fullTileXChange, 0), // left
            new Vector(-halfTileXChange, fullTileYChange), // top left
            new Vector(halfTileXChange, fullTileYChange), // top right
            new Vector(fullTileXChange, 0), // right
            new Vector(halfTileXChange, -fullTileYChange), // bottom right
            new Vector(-halfTileXChange, -fullTileYChange), // bottom left
    };

    private static Stream<Vector> createCurrentDirectionStream() {
        return Arrays.stream(possibleMoveDirections);
    }

    // I don't see a use case other than in this class, so it doesn't get its own file
    static Stream<Coordinates> getPossibleTargetCoordsForPenguinInDirection(@Nonnull BoardPeek board,
                                                                      @Nonnull Coordinates startCoordinate,
                                                                      @Nonnull Vector direction) {
        return Stream.iterate(startCoordinate.plus(direction),
                coordinates -> GameRuleLogic.canMoveTo(board, coordinates),
                coordinates -> coordinates.plus(direction));
    }


    static Stream<Move> getPossibleMovesForPenguin(@Nonnull BoardPeek board,
                                                           @Nonnull Coordinates startCoordinate) {
        @Nonnull Stream<Vector> directions = createCurrentDirectionStream();
        Stream<Stream<Coordinates>> targetStreamStream =
                directions.map(direction -> getPossibleTargetCoordsForPenguinInDirection(board, startCoordinate, direction));
        Stream<Coordinates> targetStream = targetStreamStream.flatMap(target -> target);
        return targetStream.map(target -> new Move(startCoordinate, target));
    }

    static Stream<Move> getPossibleMoves(BoardPeek board, ITeam team) {
        Stream<Pair<Coordinates, Team>> penguinStream = board.getPenguins().stream();
        Stream<Pair<Coordinates, Team>> ownPenguinStream = penguinStream.filter(
                coordinatesTeamPair -> coordinatesTeamPair.getSecond() == team);
        Stream<Stream<Move>> moveStreamStream =
                ownPenguinStream.map(coordTeamPair -> getPossibleMovesForPenguin(board, coordTeamPair.getFirst()));
        return moveStreamStream.flatMap(move -> move);
    }

    public PossibleMoveStreamFactory(BoardPeek board, ITeam team) {
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
