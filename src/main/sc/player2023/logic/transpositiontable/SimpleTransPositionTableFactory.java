package sc.player2023.logic.transpositiontable;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SimpleTransPositionTableFactory implements TransPositionTableFactory {

    static final GameStateSelector GAME_STATE_SELECTOR = gameState -> true;
    public static final GameStateHolderFactory GAME_STATE_HOLDER_FACTORY = TrivialGameStateHolder::new;

    @NotNull
    @Override
    public TransPositionTable createTransPositionTableFromDepth(int depth) {
        return new TransPositionTable(new HashMap<>(), GAME_STATE_HOLDER_FACTORY, GAME_STATE_SELECTOR);
    }
}
