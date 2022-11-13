package sc.player2023.logic.rating;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;

import javax.annotation.Nonnull;
import java.util.Collection;

public class DistanceRater implements Rater {
    public static int getDistance(Coordinates coordinates1, Coordinates coordinates2) {
        return Math.abs(coordinates1.getX() - coordinates2.getX()) + Math.abs(
                coordinates2.getY() - coordinates1.getY());
    }

    public static int getCombinedDistancesToOtherPenguins(Pair<Coordinates, Team> currentPenguin,
                                                   Collection<Pair<Coordinates, Team>> penguins) {
        int result = 0;
        for (Pair<Coordinates, Team> currentOtherPenguin : penguins) {
            if (currentOtherPenguin.equals(currentPenguin)
                    || currentOtherPenguin.getSecond() != currentPenguin.getSecond()) {
                continue;
            }
            result += getDistance(currentPenguin.getFirst(), currentOtherPenguin.getFirst());
        }
        return result;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        Collection<Pair<Coordinates, Team>> penguins = board.getPenguins();
        Rating result = Rating.ZERO;
        for (Pair<Coordinates, Team> currentPenguin : penguins) {
            int combinedDistance = getCombinedDistancesToOtherPenguins(currentPenguin, penguins);
            if (currentPenguin.getSecond() == gameState.getCurrentTeam()) {
                result = result.add(combinedDistance);
            }
            else {
                result = result.subtract(combinedDistance);
            }
        }
        return result;
    }
}
