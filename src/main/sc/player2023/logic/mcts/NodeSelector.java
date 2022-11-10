package sc.player2023.logic.mcts;

import javax.annotation.Nonnull;
import java.util.List;

public interface NodeSelector {
    @Nonnull
    List<Integer> select(MCTSTreeNode rootNode);
}
