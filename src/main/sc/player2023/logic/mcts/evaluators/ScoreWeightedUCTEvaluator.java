package sc.player2023.logic.mcts.evaluators;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.Statistics;

public class ScoreWeightedUCTEvaluator implements NodeEvaluator {

    private final double explorationWeight;

    public ScoreWeightedUCTEvaluator(double explorationWeight) {
        this.explorationWeight = explorationWeight;
    }

    /**
     * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * <a href="https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation">Monte Carlo tree search#Exploration and exploitation</a>
     */
    private double uctEvaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        long parentNodeVisits = parentNode.getStatistics().visits();

        Statistics statistics = childNode.getStatistics();
        double nodeWins = statistics.wins();
        double nodeVisits = statistics.visits();

        double exploitation = nodeWins / nodeVisits;
        double exploration  = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + this.explorationWeight * exploration;
    }

    @Override
    public double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        ImmutableGameState gameState = childNode.getGameState();
        ITeam currentTeam = gameState.getCurrentTeam().opponent();
        int pointsForCurrentTeam = gameState.getPointsForTeam(currentTeam);

        // The pure UCT value is multiplied by the points of the parent nodes team
        // in the game state of the child node. Therefore, paths with more points
        // for the parent nodes team are explored and exploited.
        return uctEvaluateNode(parentNode, childNode) * pointsForCurrentTeam;
    }
}
