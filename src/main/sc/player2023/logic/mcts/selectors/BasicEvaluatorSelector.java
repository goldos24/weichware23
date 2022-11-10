package sc.player2023.logic.mcts.selectors;

import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.NodeSelector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicEvaluatorSelector implements NodeSelector {

    @Nonnull
    private final NodeEvaluator evaluator;

    public BasicEvaluatorSelector(@Nonnull NodeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    private int pickChildNode(MCTSTreeNode parentNode) {
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
            double value = this.evaluator.evaluateNode(parentNode, childNode);

            if (value > bestValue) {
                bestValue = value;
                bestIndex = i;
            }
        }

        // Evaluator always yielded NaN
        if (bestIndex == -1) {
            return (int)Math.round(Math.random() * (children.size() - 1));
        }

        return bestIndex;
    }

    @Nonnull
    @Override
    public List<Integer> select(MCTSTreeNode rootNode) {
        assert rootNode.hasChildren();

        // Select a random child node of the root
        List<MCTSTreeNode> children = rootNode.getChildren();
        int randomIndex = (int)Math.round(Math.random() * (children.size() - 1));
        MCTSTreeNode currentNode = children.get(randomIndex);

        List<Integer> indices = new ArrayList<>();
        indices.add(randomIndex);

        // Pick successive children nodes from the randomly selected node
        while (currentNode.hasChildren()) {
            int childIndex = this.pickChildNode(currentNode);
            indices.add(childIndex);
            List<MCTSTreeNode> currentChildren = currentNode.getChildren();

            currentNode = currentChildren.get(childIndex);
        }

        return indices;
    }
}
