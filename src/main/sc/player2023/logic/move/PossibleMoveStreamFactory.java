package sc.player2023.logic.move;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Vector;
import sc.player2023.Direction;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.Penguin;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static sc.player2023.logic.GameRuleLogic.createCurrentDirectionStream;

public class PossibleMoveStreamFactory {

    @Nonnull
    public static Stream<Coordinates> getPossibleTargetCoordsForPenguinInDirection(@Nonnull BoardPeek board,
                                                                                   @Nonnull Coordinates startCoordinate,
                                                                                   @Nonnull Direction direction) {
        Vector directionVector = direction.getVector();
        return Stream.iterate(startCoordinate.plus(directionVector),
                              coordinates -> GameRuleLogic.canMoveTo(board, coordinates),
                              coordinates -> coordinates.plus(directionVector));
    }

    @Nonnull
    public static Stream<Move> getPossibleMovesForPenguin(@Nonnull BoardPeek board,
                                                          @Nonnull Coordinates startCoordinate) {
        @Nonnull Stream<Direction> directions = createCurrentDirectionStream();
        return getMoveStreamForPenguinFromDirections(board, startCoordinate, directions);
    }

    @Nonnull
    private static Stream<Move> getMoveStreamForPenguinFromDirections(@Nonnull BoardPeek board,
                                                                      @Nonnull Coordinates startCoordinate,
                                                                      @Nonnull Stream<Direction> directions) {
        Stream<Stream<Coordinates>> targetStreamStream =
                directions.map(
                        direction -> getPossibleTargetCoordsForPenguinInDirection(board, startCoordinate, direction));
        Stream<Coordinates> targetStream = targetStreamStream.flatMap(target -> target);
        return targetStream.map(target -> new Move(startCoordinate, target));
    }

    @Nonnull
    public static Stream<Move> getPossibleMovesInNormalCase(BoardPeek board, ITeam team) {
        Stream<Penguin> penguinStream = board.getPenguins().stream();
        return getPossibleMovesInNormalCaseFromPenguins(board, team, penguinStream);
    }

    @Nonnull
    private static Stream<Move> getPossibleMovesInNormalCaseFromPenguins(BoardPeek board, ITeam team,
                                                                         Stream<Penguin> penguinStream) {
        Stream<Penguin> ownPenguinStream = penguinStream.filter(
                coordinatesTeamPair -> coordinatesTeamPair.team() == team);
        Stream<Stream<Move>> moveStreamStream =
                ownPenguinStream.map(coordTeamPair -> getPossibleMovesForPenguin(board, coordTeamPair.coordinates()));
        return moveStreamStream.flatMap(move -> move);
    }

    @Nonnull
    public static Stream<Move> getPossibleMovesAtBeginning(BoardPeek board) {
        return GameRuleLogic.createBoardCoordinateStream().filter(coordinates -> board.get(coordinates).getFish() == 1).
                            map(target -> new Move(null, target));
    }

    @Nonnull
    public static Stream<Move> getPossibleMoves(BoardPeek board, ITeam team) {
        return GameRuleLogic.allPenguinsPlaced(board, team) ?
                getPossibleMovesInNormalCase(board, team) : getPossibleMovesAtBeginning(board);
    }
}
