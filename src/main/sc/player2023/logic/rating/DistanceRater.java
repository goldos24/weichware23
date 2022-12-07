package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.player2023.logic.Penguin;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.Collection;

public class DistanceRater implements Rater {
    public static int getDistance(Coordinates coordinates1, Coordinates coordinates2) {
        return Math.abs(coordinates1.getX() - coordinates2.getX()) + Math.abs(
                coordinates2.getY() - coordinates1.getY());
    }

    public static int getCombinedDistancesToOtherPenguins(Penguin currentPenguin,
                                                   Collection<Penguin> penguins) {
        int result = 0;
        for (Penguin currentOtherPenguin : penguins) {
            if (currentOtherPenguin.equals(currentPenguin)
                    || currentOtherPenguin.team() != currentPenguin.team()) {
                continue;
            }
            result += getDistance(currentPenguin.coordinates(), currentOtherPenguin.coordinates());
        }
        return result;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        Collection<Penguin> penguins = board.getPenguins();
        Rating result = Rating.ZERO;
        for (Penguin currentPenguin : penguins) {
            int combinedDistance = getCombinedDistancesToOtherPenguins(currentPenguin, penguins);
            if (currentPenguin.team() == gameState.getCurrentTeam()) {
                result = result.add(combinedDistance);
            }
            else {
                result = result.subtract(combinedDistance);
            }
        }
        return result;
    }
}
