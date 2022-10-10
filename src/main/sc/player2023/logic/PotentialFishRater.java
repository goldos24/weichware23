package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import java.util.List;

public class PotentialFishRater implements Rater {
    @Override
    public int rate(@NotNull ImmutableGameState gameState) {
        int result = 0;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        Board board = gameState.gameState().getBoard();
        for(Move move : possibleMoves) {
            Field moveTargetField = board.get(move.getTo());
            result += moveTargetField.getFish();
        }
        return result;
    }
}
