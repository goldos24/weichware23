package sc.player2023.logic.rating;

import org.jetbrains.annotations.Nullable;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Stream;

public class PenguinCutOffRater implements Rater {

    private static final Map<Long, Integer> possibleTargetCountRatings = Map.of(0L, 50,
                                                                                1L, 2,
                                                                                2L, 1);

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        Rating result = Rating.ZERO;
        BoardPeek board = gameState.getBoard();
        ITeam team = gameState.getCurrentTeam();
        for (var penguin : board.getPenguins()) {
            Rating prefix = new Rating(penguin.team() == team ? -1 : 1);
            var coords = penguin.coordinates();
            Stream<Coordinates> possibleTargets = GameRuleLogic
                    .createCurrentDirectionStream().
                    map(direction -> coords.plus(direction.getVector()))
                    .filter(coordinates -> GameRuleLogic.canMoveTo(board, coordinates));
            long possibleTargetCount = possibleTargets.count();
            @Nullable Integer targetCountRating = possibleTargetCountRatings.get(possibleTargetCount);
            if (targetCountRating != null) {
                result = result.add(prefix.multiply(targetCountRating));
            }
        }
        return result;
    }
}
