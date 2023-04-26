package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class TrivialGameStateHolder implements GameStateHolder {
    @Nonnull
    private final ImmutableGameState gameState;

    public TrivialGameStateHolder(@Nonnull ImmutableGameState gameState) {
        this.gameState = gameState;
    }

    @Nonnull
    @Override
    public ImmutableGameState getGameState() {
        return gameState;
    }

    @Nonnull
    public ImmutableGameState gameState() {
        return gameState;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TrivialGameStateHolder) obj;
        return Objects.equals(this.gameState, that.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState);
    }

    @Override
    public String toString() {
        return "TrivialGameStateHolder[" +
                "gameState=" + gameState + ']';
    }

}
