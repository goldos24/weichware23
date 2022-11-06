package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ReachableFishRater implements Rater {
    static Set<Coordinates> getReachableCoordsFromCoordinate(Coordinates startCoords, BoardPeek board) {
        Set<Coordinates> result = new HashSet<>();
        addReachableCoordsToSet(result, startCoords, board);
        return result;
    }

    static void addReachableCoordsToSet(Set<Coordinates> set, Coordinates startCoords, BoardPeek board) {
        if(set.contains(startCoords)) {
            return;
        }
        set.add(startCoords);
        Stream<Coordinates> directlyReachableCoords = GameRuleLogic.createCurrentDirectionStream().map(startCoords::plus);
        directlyReachableCoords.forEach(coordinates -> {
            if(!GameRuleLogic.coordsValid(coordinates)) {
                return;
            }
            if(board.get(coordinates).getFish() == 0) {
                return;
            }
            addReachableCoordsToSet(set, coordinates, board);
        });
    }

    static int getReachableFishFromCoordinate(Coordinates startCoords, BoardPeek board) {
        Set<Coordinates> coords = getReachableCoordsFromCoordinate(startCoords, board);
        return getReachableFishFromCoordSet(board, coords);
    }

    private static int getReachableFishFromCoordSet(BoardPeek board, Set<Coordinates> coords) {
        int result = 0;
        for(Coordinates coord : coords) {
            result += board.get(coord).getFish();
        }
        return result;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam otherTeam = gameState.getCurrentTeam();
        Map<ITeam, Set<Coordinates>> reachableCoords = Map.of(Team.ONE, new HashSet<>(), Team.TWO, new HashSet<>());
        for(var penguin : board.getPenguins()) {
            Coordinates penguinPosition = penguin.getFirst();
            Set<Coordinates> ownSet = reachableCoords.get(penguin.getSecond());
            addReachableCoordsToSet(ownSet, penguinPosition, board);
        }
        Rating ownRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(ownTeam)));
        Rating opponentRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(otherTeam)));
        return ownRating.subtract(opponentRating);
    }
}
