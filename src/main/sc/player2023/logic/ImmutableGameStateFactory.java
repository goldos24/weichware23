package sc.player2023.logic;

import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public class ImmutableGameStateFactory {
    @Nonnull
    public static ImmutableGameState createAny() {
        GameState gameState = new GameState();
        return new  ImmutableGameState(gameState);
    }
}
