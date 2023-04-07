package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ReachableFishRater implements Rater {

    static Set<Coordinates> getReachableCoordsFromCoordinate(Coordinates startCoords, BoardPeek board) {
        Set<Coordinates> result = new HashSet<>();
        boolean[] array = new boolean[ARRAY_SIZE];
        addReachableCoordsToSet(array, startCoords, board);
        for (int i = 0; i < array.length; ++i) {
            if (array[i]) {
                result.add(GameRuleLogic.indexToCoords(i));
            }
        }
        return result;
    }

    static void addReachableCoordsToSet(boolean[] set, Coordinates startCoords, BoardPeek board) {
        int index = GameRuleLogic.coordsToIndex(startCoords);
        if (set[index]) {
            return;
        }
        set[index] = true;
        Stream<Coordinates> directlyReachableCoords = GameRuleLogic
                .createCurrentDirectionStream()
                .map(direction -> startCoords.plus(direction.getVector()));
        directlyReachableCoords.forEach(coordinates -> {
            if (!GameRuleLogic.coordsValid(coordinates)) {
                return;
            }
            if (board.get(coordinates).getFish() == 0) {
                return;
            }
            addReachableCoordsToSet(set, coordinates, board);
        });
    }

    static int getReachableFishFromCoordinate(Coordinates startCoords, BoardPeek board) {
        Set<Coordinates> coords = getReachableCoordsFromCoordinate(startCoords, board);
        boolean[] array = new boolean[ARRAY_SIZE];
        for (Coordinates coordinates : coords) {
            array[GameRuleLogic.coordsToIndex(coordinates)] = true;
        }
        return getReachableFishFromCoordSet(board, array);
    }

    private static int getReachableFishFromCoordSet(BoardPeek board, boolean[] coords) {
        int result = 0;
        for (int i = 0; i < coords.length; ++i) {
            if (coords[i]) {
                result += board.get(GameRuleLogic.indexToCoords(i)).getFish();
            }
        }
        return result;
    }

    private static final int ARRAY_SIZE = GameRuleLogic.BOARD_HEIGHT * GameRuleLogic.BOARD_WIDTH;

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam otherTeam = ownTeam.opponent();
        Map<ITeam, boolean[]> reachableCoords = Map.of(Team.ONE, new boolean[ARRAY_SIZE], Team.TWO,
                                                       new boolean[ARRAY_SIZE]);
        for (var penguin : board.getPenguins()) {
            Coordinates penguinPosition = penguin.coordinates();
            boolean[] ownSet = reachableCoords.get(penguin.team());
            addReachableCoordsToSet(ownSet, penguinPosition, board);
        }
        Rating ownRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(ownTeam)));
        Rating opponentRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(otherTeam)));
        return ownRating.subtract(opponentRating);
    }
}
