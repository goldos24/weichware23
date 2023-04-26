package sc.player2023.logic.transpositiontable;
import org.jetbrains.annotations.NotNull;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;

public final class TransPositionTable {
    @Nonnull
    private final HashMap<GameStateHolder, TransPositionTableEntry> contents;
    @Nonnull
    private final GameStateHolderFactory gameStateHolderFactory;
    @Nonnull
    private final GameStateSelector selector;

    public TransPositionTable(@Nonnull HashMap<GameStateHolder, Rating> exactValueContents,
                              @Nonnull GameStateHolderFactory gameStateHolderFactory,
                              @Nonnull GameStateSelector selector) {

        this.contents = new HashMap<>();
        for(var entry : exactValueContents.entrySet()) {
            contents.put(entry.getKey(), new TransPositionTableEntry(entry.getValue(), RatingType.EXACT));
        }
        this.gameStateHolderFactory = gameStateHolderFactory;
        this.selector = selector;
    }

    public void addExact(@Nonnull ImmutableGameState gameState, @Nonnull Rating rating) {
        TransPositionTableEntry entry = new TransPositionTableEntry(rating, RatingType.EXACT);
        add(gameState, entry);
    }

    public void add(@Nonnull ImmutableGameState gameState, @Nonnull TransPositionTableEntry entry) {
        if(!selector.toBeSaved(gameState))
            return;
        contents.put(gameStateHolderFactory.holderFromGameState(gameState), entry);
    }

    public boolean hasGameState(@Nonnull ImmutableGameState gameState) {
        GameStateHolder key = gameStateHolderFactory.holderFromGameState(gameState);
        return contents.containsKey(key);
    }

    public boolean hasGameStateWithExactRating(@Nonnull ImmutableGameState gameState) {
        if(!hasGameState(gameState)) {
            return false;
        }
        GameStateHolder key = gameStateHolderFactory.holderFromGameState(gameState);
        return contents.get(key).ratingType() == RatingType.EXACT;
    }

    public Rating getRatingForGameState(@Nonnull ImmutableGameState gameState) {
        return getTransPositionTableEntry(gameState).rating();
    }

    public TransPositionTableEntry getTransPositionTableEntry(@NotNull ImmutableGameState gameState) {
        return contents.get(gameStateHolderFactory.holderFromGameState(gameState));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransPositionTable )) {
            return false;
        }
        TransPositionTable that = (TransPositionTable) o;
        return contents.equals(that.contents)
                && gameStateHolderFactory.equals(that.gameStateHolderFactory)
                && selector.equals(that.selector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, gameStateHolderFactory, selector);
    }
}