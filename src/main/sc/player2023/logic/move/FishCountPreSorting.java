package sc.player2023.logic.move;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Till Fransson
 * @since 25.04.2023
 */
public class FishCountPreSorting {

    private FishCountPreSorting() {
    }
    
    @Nonnull
    public static List<Move> getPossibleMoves(@Nonnull ImmutableGameState gameState) {
        List<Move> possibleMoves = new ArrayList<>(GameRuleLogic.getPossibleMoves(gameState));
        BoardPeek board = gameState.getBoard();
        possibleMoves.sort((a, b) -> {
            Field fieldA = board.get(a.getTo());
            int fieldAFish = fieldA.getFish();
            Field fieldB = board.get(b.getTo());
            int fieldBFish = fieldB.getFish();
            return Integer.compare(fieldBFish, fieldAFish);
        });
        return possibleMoves;
    }

}
