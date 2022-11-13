package sc.player2023.logic.gameState;

import javax.annotation.Nonnull;

public class GameStateWithContinuation {
    @Nonnull
    ImmutableGameState gameState;
    private final boolean shouldContinue;

    private GameStateWithContinuation(@Nonnull ImmutableGameState gameState, boolean shouldContinue) {
        this.gameState = gameState;
        this.shouldContinue = shouldContinue;
    }

    public @Nonnull
    ImmutableGameState getGameState() {
        return gameState;
    }

    public boolean shouldContinueAlgorithm() {
        return shouldContinue;
    }

    public boolean shouldStopAlgorithm() {
        return !shouldContinue;
    }

    public static GameStateWithContinuation continueAlgorithm(@Nonnull ImmutableGameState gameState) {
        return new GameStateWithContinuation(gameState, true);
    }

    public static GameStateWithContinuation stopAlgorithm(@Nonnull ImmutableGameState gameState) {
        return new GameStateWithContinuation(gameState, false);
    }
}
