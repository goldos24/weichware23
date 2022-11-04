package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static sc.player2023.logic.GameRuleLogic.createCurrentDirectionStream;

@SuppressWarnings("unused")
public class PossibleMoveStreamFactory{
    public static Stream<Coordinates> getPossibleTargetCoordsForPenguinInDirection(@Nonnull BoardPeek board,
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

    public static Stream<Move> getPossibleMovesInNormalCase(BoardPeek board, ITeam team) {
        Stream<Pair<Coordinates, Team>> penguinStream = board.getPenguins().stream();
        Stream<Pair<Coordinates, Team>> ownPenguinStream = penguinStream.filter(
                coordinatesTeamPair -> coordinatesTeamPair.getSecond() == team);
        Stream<Stream<Move>> moveStreamStream =
                ownPenguinStream.map(coordTeamPair -> getPossibleMovesForPenguin(board, coordTeamPair.getFirst()));
        return moveStreamStream.flatMap(move -> move);
    }

    public static Stream<Move> getPossibleMovesAtBeginning(BoardPeek board) {
        return GameRuleLogic.createBoardCoordinateStream().filter(coordinates -> board.get(coordinates).getFish() == 1).
                map(target -> new Move(null, target));
    }

    public static Stream<Move> getPossibleMoves(BoardPeek board, ITeam team) {
        return GameRuleLogic.allPenguinsPlaced(board, team) ?
                getPossibleMovesInNormalCase(board, team) : getPossibleMovesAtBeginning(board);
    }
}
