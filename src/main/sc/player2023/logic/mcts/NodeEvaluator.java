package sc.player2023.logic.mcts;

public interface NodeEvaluator {
    double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode);
}
