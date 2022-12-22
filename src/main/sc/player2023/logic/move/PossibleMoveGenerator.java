package sc.player2023.logic.move;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 22.12.2022
 */
public interface PossibleMoveGenerator {
    
    @Nonnull
    Iterable<Move> getPossibleMoves(@Nonnull ImmutableGameState gameState);
}
