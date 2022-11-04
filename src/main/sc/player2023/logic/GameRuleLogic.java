package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(ImmutableGameState gameState, Move move) {
        ITeam team = gameState.getCurrentTeam();
        var teamPointsMap = gameState.getTeamPointsMap();
        var targetField = gameState.getBoard().get(move.getTo());
        Integer ownPoints = teamPointsMap.get(team);
        Integer opponentPoints = teamPointsMap.get(team.opponent());
        assert ownPoints != null;
        assert opponentPoints != null;
        teamPointsMap = ImmutableMap.<ITeam, Integer>builder().
                put(team.opponent(), opponentPoints).
                put(team, ownPoints + targetField.getFish()).
                build();
        BoardPeek newBoard = gameState.getBoard().withMovePerformed(move, gameState.getCurrentTeam());
        return new ImmutableGameState(teamPointsMap, newBoard, gameState.getCurrentTeam().opponent());
    }


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

    public static Stream<Vector> createCurrentDirectionStream() {
        return Arrays.stream(possibleMoveDirections);
    }

    public static boolean anyPossibleMovesForPlayer(@Nonnull BoardPeek board, @Nonnull ITeam team) {
        return PossibleMoveStreamFactory.getPossibleMoves(board, team).findFirst().isPresent();
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return getPossibleMoveStream(gameState).toList();
    }

    public static Stream<Move> getPossibleMoveStream(ImmutableGameState gameState) {
        return PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(),gameState.getCurrentTeam());
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        return gameState.getPointsForTeam(team) > gameState.getPointsForTeam(team.opponent());
    }

    private static final int MAX_PENGUIN_COUNT_FOR_SINGLE_TEAM = 4;
    public static boolean allPenguinsPlaced(@Nonnull BoardPeek board, @Nonnull ITeam team) {
        Stream<Pair<Coordinates, Team>> ownPenguins =
                board.getPenguins().stream().filter(coordinatesTeamPair -> coordinatesTeamPair.getSecond() == team);
        return ownPenguins.count() >= MAX_PENGUIN_COUNT_FOR_SINGLE_TEAM;
    }

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    public static final int RIGHTMOST_X = BOARD_WIDTH*2-1;

    public static boolean coordsValid(Coordinates coordinates) {
        if(coordinates == null)
            return false;
        int x = coordinates.getX();
        int y = coordinates.getY();
        return x >= 0 && x <= RIGHTMOST_X && y >= 0 && y < BOARD_HEIGHT && x % 2 == y % 2;
    }

    public static Coordinates nextCoord(Coordinates coordinates) {
        int nextXInLine = coordinates.getX() + 2;
        int nextY = coordinates.getY() + 1;
        boolean xOverflow = nextXInLine > RIGHTMOST_X;
        int realNextX = xOverflow ? nextY % 2 : nextXInLine;
        return new Coordinates(realNextX, xOverflow ? nextY : coordinates.getY());
    }

    public static Stream<Coordinates> createBoardCoordinateStream() {
        return Stream.iterate(new Coordinates(0, 0), GameRuleLogic::coordsValid, GameRuleLogic::nextCoord);
    }

    public static boolean canMoveTo(@Nonnull BoardPeek board, @Nonnull Coordinates target) {
        return coordsValid(target) && board.get(target).getFish() != 0;
    }
}
