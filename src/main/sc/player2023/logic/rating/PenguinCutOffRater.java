package sc.player2023.logic.rating;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;

import java.util.Map;
import java.util.stream.Stream;

public class PenguinCutOffRater implements Rater {

    private static final Map<Long, Double> possibleTargetCountRatings = Map.of(0L, 1.0, 1L, 0.5, 2L, 0.1);

    @Override
    public Rating rate(@NotNull ImmutableGameState gameState) {
        Rating result = Rating.ZERO;
        BoardPeek board = gameState.getBoard();
        ITeam team = gameState.getCurrentTeam();
        for(var penguin : board.getPenguins()) {
            Rating prefix = new Rating(penguin.getSecond() == team ? -1.0 : 1.0);
            var coords = penguin.getFirst();
            Stream<Coordinates> possibleTargets = GameRuleLogic
                    .createCurrentDirectionStream().
                    map(coords::plus).filter(coordinates -> GameRuleLogic.canMoveTo(board, coordinates));
            long possibleTargetCount = possibleTargets.count();
            @Nullable Double targetCountRating = possibleTargetCountRatings.get(possibleTargetCount);
            if(targetCountRating != null) {
                result = result.add(prefix.multiply(targetCountRating));
            }
        }
        return result;
    }
}
