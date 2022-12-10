package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public interface GameStateHolderFactory {
    GameStateHolder holderFromGameState(@Nonnull ImmutableGameState gameState);
}
