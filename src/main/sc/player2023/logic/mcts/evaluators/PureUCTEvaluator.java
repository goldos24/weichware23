package sc.player2023.logic.mcts.evaluators;

import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;

public class PureUCTEvaluator implements NodeEvaluator {

    @Override
    public double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        int visits = parentNode.getStatistics().visits();
        return childNode.uct(visits);
    }
}
