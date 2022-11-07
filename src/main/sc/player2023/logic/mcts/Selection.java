package sc.player2023.logic.mcts;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Selection {

    @Nonnull
    private final ImmutableMCTSTreeNode parentNode;

    public Selection(@Nonnull ImmutableMCTSTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    private static int pickChildNode(ImmutableMCTSTreeNode node) {
        int parentNodeVisits = node.getStatistics().visits();
        List<ImmutableMCTSTreeNode> children = node.getChildren();

        assert children.size() > 0;

        // Only one child to select
        if (children.size() == 1) {
            return 0;
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        int bestIndex = -1;

        for (int i = 0; i < children.size(); ++i) {
            ImmutableMCTSTreeNode child = children.get(i);
            double value = child.uct(parentNodeVisits);

            if (value > bestValue) {
                bestValue = value;
                bestIndex = i;
            }
        }

        // UCT always yielded NaN
        if (bestIndex == -1) {
            throw new IllegalArgumentException("No valid node index found");
        }

        return bestIndex;
    }

    @Nonnull
    public List<Integer> complete() {
        ImmutableMCTSTreeNode currentNode = this.parentNode;
        List<Integer> indices = new ArrayList<>();

        while (currentNode.hasChildren()) {
            int childIndex = pickChildNode(currentNode);
            indices.add(childIndex);
            List<ImmutableMCTSTreeNode> currentChildren = currentNode.getChildren();
            currentNode = currentChildren.get(childIndex);
        }

        return indices;
    }
}
