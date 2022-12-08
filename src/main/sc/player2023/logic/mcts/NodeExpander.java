package sc.player2023.logic.mcts;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class NodeExpander {
    @Nonnull
    private final NodeExpansionProvider nodeProvider;

    public NodeExpander(@Nonnull NodeExpansionProvider nodeProvider) {
        this.nodeProvider = nodeProvider;
    }

    @Nonnull
    public NodeExpansionProvider getNodeProvider() {
        return nodeProvider;
    }

    @Nonnull
    public abstract List<MCTSTreeNode> createChildren(@Nonnull MCTSTreeNode node);

    public abstract boolean canExpand(@Nonnull MCTSTreeNode node);
}
