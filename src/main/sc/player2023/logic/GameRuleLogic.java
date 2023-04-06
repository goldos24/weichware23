package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.Direction;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.score.PlayerScore;
import sc.player2023.logic.score.Score;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameRuleLogic {
    @Nonnull
    public static ImmutableGameState withMovePerformed(@Nonnull ImmutableGameState gameState, @Nonnull Move move) {
        BoardPeek board = gameState.getBoard();
        Coordinates to = move.getTo();
        var targetField = board.get(to);
        PlayerScore playerScoreTeamOne = gameState.getPlayerScoreForTeam(Team.ONE);
        PlayerScore playerScoreTeamTwo = gameState.getPlayerScoreForTeam(Team.TWO);
        var gameScore = new GameScore(playerScoreTeamOne, playerScoreTeamTwo);
        int addedPoints = targetField.getFish();
        ITeam currentTeam = gameState.getCurrentTeam();
        PlayerScore currentPlayerScore = gameScore.getPlayerScoreFromTeam(currentTeam);
        PlayerScore newPlayerScore = currentPlayerScore.add(addedPoints);
        gameScore = gameScore.withPlayerScore(newPlayerScore);
        BoardPeek newBoard = board.withMovePerformed(move, currentTeam);
        return new ImmutableGameState(gameScore, newBoard, currentTeam.opponent());
    }

    public static int getRandomMoveIndex(@Nonnull List<Move> possibleMoves) {
        return (int) Math.round(Math.random() * (possibleMoves.size() - 1));
    }

    public static int getEmptyFieldCountFromBoard(@Nonnull BoardPeek board) {
        return createBoardCoordinateStream().map(board::get).mapToInt(field -> field.isEmpty() ? 1 : 0).sum();
    }

    @Nonnull
    public static ImmutableGameState withRandomMovePerformed(@Nonnull ImmutableGameState gameState) {
        List<Move> moves = GameRuleLogic.getPossibleMoves(gameState);
        int randomMoveIndex = getRandomMoveIndex(moves);
        Move randomMove = moves.get(randomMoveIndex);
        return GameRuleLogic.withMovePerformed(gameState, randomMove);
    }


    public static Stream<Direction> createCurrentDirectionStream() {
        return Arrays.stream(Direction.values());
    }

    public static boolean anyPossibleMovesForPlayer(@Nonnull BoardPeek board, @Nonnull ITeam team) {
        return PossibleMoveStreamFactory.getPossibleMoves(board, team).findFirst().isPresent();
    }

    public static List<Move> getPossibleMoves(ImmutableGameState gameState) {
        return getPossibleMoveStream(gameState).toList();
    }

    public static Stream<Move> getPossibleMoveStream(ImmutableGameState gameState) {
        return PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), gameState.getCurrentTeam());
    }

    public static boolean isTeamWinner(ImmutableGameState gameState, ITeam team) {
        Score score = gameState.getScoreForTeam(team);
        Score opponentScore = gameState.getScoreForTeam(team.opponent());
        return score.isGreaterThan(opponentScore);
    }

    private static final int MAX_PENGUIN_COUNT_FOR_SINGLE_TEAM = 4;

    public static boolean allPenguinsPlaced(@Nonnull BoardPeek board, @Nonnull ITeam team) {
        Stream<Penguin> ownPenguins =
                board.getPenguins().stream().filter(coordinatesTeamPair -> coordinatesTeamPair.team() == team);
        return ownPenguins.count() >= MAX_PENGUIN_COUNT_FOR_SINGLE_TEAM;
    }

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    public static final Coordinates nonReachableBoardCenter = new Coordinates(BOARD_WIDTH-1, BOARD_HEIGHT/2);
    public static final int RIGHTMOST_X = BOARD_WIDTH * 2 - 1;

    public static boolean coordsValid(Coordinates coordinates) {
        if (coordinates == null) {
            return false;
        }
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

    public static Coordinates indexToCoords(int index) {
        int y = index / BOARD_WIDTH;
        int x = (index % BOARD_WIDTH) * 2 + y % 2;
        return new Coordinates(x, y);
    }

    public static int coordsToIndex(Coordinates coordinates) {
        return coordinates.getY() * BOARD_WIDTH + coordinates.getX() / 2;
    }

    public static boolean isCollinear(@Nonnull Vector start, @Nonnull Vector end) {
        return start.getDx() * end.getDy() == start.getDy() * end.getDx();
    }
}
