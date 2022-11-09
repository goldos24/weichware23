package sc.player2023.logic.mcts.evaluators;

import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.Statistics;

public class PureUCTEvaluator implements NodeEvaluator {

    private final double explorationWeight;

    public PureUCTEvaluator(double explorationWeight) {
        this.explorationWeight = explorationWeight;
    }

    /**
     * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * <a href="https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation">Monte Carlo tree search#Exploration and exploitation</a>
     */
    @Override
    public double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        long parentNodeVisits = parentNode.getStatistics().visits();

        Statistics statistics = childNode.getStatistics();
        double nodeWins = statistics.wins();
        double nodeVisits = statistics.visits();

        double exploitation = nodeWins / nodeVisits;
        double exploration  = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + this.explorationWeight * exploration;
    }
}
