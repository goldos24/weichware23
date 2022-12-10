package sc.player2023.logic.transpositiontable;

import javax.annotation.Nonnull;

public interface TransPositionTableFactory {
    @Nonnull TransPositionTable createTransPositionTableFromDepth(int depth);
}
