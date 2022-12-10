package sc.player2023.logic.transpositiontable;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;
import java.util.HashMap;

public final class TransPositionTable {
    @Nonnull
    private final HashMap<GameStateHolder, Rating> contents;
    @Nonnull
    private final GameStateHolderFactory gameStateHolderFactory;
    @Nonnull
    private final GameStateSelector selector;

    public TransPositionTable(@Nonnull HashMap<GameStateHolder, Rating> contents,
                              @Nonnull GameStateHolderFactory gameStateHolderFactory,
                              @Nonnull GameStateSelector selector) {
        this.contents = contents;
        this.gameStateHolderFactory = gameStateHolderFactory;
        this.selector = selector;
    }

    public void add(@Nonnull ImmutableGameState gameState, @Nonnull Rating rating) {
        if(!selector.toBeSaved(gameState))
            return;
        contents.put(gameStateHolderFactory.holderFromGameState(gameState), rating);
    }

    public boolean hasGameState(@Nonnull ImmutableGameState gameState) {
        return contents.containsKey(gameStateHolderFactory.holderFromGameState(gameState));
    }

    public Rating getRatingForGameState(@Nonnull ImmutableGameState gameState) {
        return contents.get(gameStateHolderFactory.holderFromGameState(gameState));
    }
}