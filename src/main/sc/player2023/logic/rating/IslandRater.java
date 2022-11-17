package sc.player2023.logic.rating;

import sc.player2023.logic.board.Island;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 17.11.2022
 */
public interface IslandRater {
    
    Rating rate(@Nonnull ImmutableGameState gameState, @Nonnull Island island);
}
