package sc.player2023.logic.transpositiontable;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SimpleTransPositionTableFactory implements TransPositionTableFactory {

    @NotNull
    @Override
    public TransPositionTable createTransPositionTableFromDepth(int depth) {
        return new TransPositionTable(new HashMap<>(), TrivialGameStateHolder::new, gameState -> true);
    }
}
