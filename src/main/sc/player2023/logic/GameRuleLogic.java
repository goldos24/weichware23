package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(ImmutableGameState gameState, Move move) {
        GameState realGameState = gameState.getGameState();
        ITeam team = realGameState.getCurrentTeam();
        var teamPointsMap = gameState.getTeamPointsMap();
        var targetField = realGameState.getBoard().get(move.getTo());
        Integer ownPoints = teamPointsMap.get(team);
        Integer opponentPoints = teamPointsMap.get(team.opponent());
        assert ownPoints != null;
        assert opponentPoints != null;
        teamPointsMap = ImmutableMap.<ITeam, Integer>builder().
                put(team.opponent(), opponentPoints).
                put(team, ownPoints + targetField.getFish()).
                build();
        realGameState.performMove(move);
        return new ImmutableGameState(realGameState, teamPointsMap);
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return gameState.getGameState().getPossibleMoves();
    }

    public static Stream<Move> getPossibleMoveStream(ImmutableGameState gameState) {
        return PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(),gameState.getCurrentTeam());
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        return RatingUtil.isTeamWinnerAfterGameEnd(gameState.getGameState(), team);
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
