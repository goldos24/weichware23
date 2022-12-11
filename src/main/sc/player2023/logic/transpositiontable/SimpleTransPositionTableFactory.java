package sc.player2023.logic.transpositiontable;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class SimpleTransPositionTableFactory implements TransPositionTableFactory{

    private static final GameStateSelector ALWAYS_TRUE_SELECTOR = gameState -> true;
    private static final GameStateSelector ALWAYS_FALSE_SELECTOR = gameState -> false;
    private static final GameStateHolderFactory GAME_STATE_HOLDER_FACTORY = TrivialGameStateHolder::new;

    @Nonnull
    @Override
    public TransPositionTable createTransPositionTableFromDepth(int depth) {
        return new TransPositionTable(new HashMap<>(), GAME_STATE_HOLDER_FACTORY, depth >=2 ? ALWAYS_TRUE_SELECTOR : ALWAYS_FALSE_SELECTOR);
    }
}
