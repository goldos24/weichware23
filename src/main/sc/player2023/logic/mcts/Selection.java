package sc.player2023.logic.mcts;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Selection {

    private Selection() {}

    private static int pickChildNode(MCTSTreeNode parentNode, NodeEvaluator evaluator) {
        List<MCTSTreeNode> children = parentNode.getChildren();

        assert children.size() > 0;

        // Only one child to select
        if (children.size() == 1) {
            return 0;
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        int bestIndex = -1;

        for (int i = 0; i < children.size(); ++i) {
            MCTSTreeNode childNode = children.get(i);
            double value = evaluator.evaluateNode(parentNode, childNode);

            if (value > bestValue) {
                bestValue = value;
                bestIndex = i;
            }
        }

        // UCT always yielded NaN
        if (bestIndex == -1) {
            return (int)Math.round(Math.random() * (children.size() - 1));
        }

        return bestIndex;
    }

    @Nonnull
    public static List<Integer> complete(MCTSTreeNode rootNode, NodeEvaluator evaluator) {
        MCTSTreeNode currentNode = rootNode;
        List<Integer> indices = new ArrayList<>();

        while (currentNode.hasChildren()) {
            int childIndex = pickChildNode(currentNode, evaluator);
            indices.add(childIndex);
            List<MCTSTreeNode> currentChildren = currentNode.getChildren();

            currentNode = currentChildren.get(childIndex);
        }

        return indices;
    }
}
