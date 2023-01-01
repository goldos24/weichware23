package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public record TrivialGameStateHolder(@Nonnull ImmutableGameState gameState) implements GameStateHolder {
    @Nonnull
    @Override
    public ImmutableGameState getGameState() {
        return gameState;
    }
}
