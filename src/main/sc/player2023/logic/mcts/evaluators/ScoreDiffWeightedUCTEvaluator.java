package sc.player2023.logic.mcts.evaluators;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.Statistics;

public class ScoreDiffWeightedUCTEvaluator implements NodeEvaluator {

    private final double explorationWeight;

    public ScoreDiffWeightedUCTEvaluator(double explorationWeight) {
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
        return exploitation + this.explorationWeight * Math.sqrt(exploration);
    }

    @Override
    public double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        ImmutableGameState gameState = childNode.getGameState();
        ITeam gameStateCurrentTeam = gameState.getCurrentTeam();
        ITeam gameStateOpponentTeam = gameStateCurrentTeam.opponent();
        int pointsForRootsCurrentTeam  = gameState.getScoreForTeam(gameStateOpponentTeam).score();
        int pointsForRootsOpponentTeam = gameState.getScoreForTeam(gameStateCurrentTeam).score();

        // The pure UCT value is multiplied by the difference between the points of the
        // parent nodes teams in the game state of the child node. Therefore, paths with
        // a larger point difference between the two teams are explored and exploited.
        return uctEvaluateNode(parentNode, childNode) * (pointsForRootsCurrentTeam - pointsForRootsOpponentTeam);
    }
}
