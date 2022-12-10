package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public interface GameStateHolder {
    @Override
    int hashCode();
    @Nonnull
    ImmutableGameState getGameState();
}
