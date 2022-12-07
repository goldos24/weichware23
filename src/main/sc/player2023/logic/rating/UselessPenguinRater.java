package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class UselessPenguinRater implements Rater {
    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        Rating result = Rating.ZERO;
        var board = gameState.getBoard();
        var team = gameState.getCurrentTeam();
        for(var penguin : board.getPenguins()) {
            var coords = penguin.coordinates();
            Stream<Coordinates> possibleTargets = GameRuleLogic
                    .createCurrentDirectionStream().
                    map(coords::plus).filter(coordinates -> GameRuleLogic.canMoveTo(board, coordinates));
            if(possibleTargets.findAny().isEmpty()) {
                result = result.add(penguin.team() == team ? -1.0 : 1.0);
            }
        }
        return result;
    }
}
