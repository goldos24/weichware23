package sc.player2023.logic.board;

import sc.player2023.logic.Penguin;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * @author Till Fransson
 * @since 17.11.2022
 * 
 * An Island is a part of the board, that cannot be reached and can only be accessed by penguins already on it.
 * 
 */
@Immutable
public record Island(int fish, @Nonnull Penguin penguin) {
    
}
