package sc.player2023.logic.move;

import sc.api.plugins.Coordinates;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
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
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        BoardPeek board = gameState.getBoard();
        for (Move possibleMove : possibleMoves) {
            Coordinates to = possibleMove.getTo();
            Field field = board.get(to);
            int fish = field.getFish();
        }
        return possibleMoves;
    }

}
