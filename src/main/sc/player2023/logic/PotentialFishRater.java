package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import java.util.List;

public class PotentialFishRater implements Rater {
    @Override
    public Rating rate(@NotNull ImmutableGameState gameState) {
        Rating result = Rating.ZERO;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        Board board = gameState.getGameState().getBoard();
        for (Move move : possibleMoves) {
            Field moveTargetField = board.get(move.getTo());
            int fish = moveTargetField.getFish();
            result = result.add(fish);
        }
        return result;
    }
}
