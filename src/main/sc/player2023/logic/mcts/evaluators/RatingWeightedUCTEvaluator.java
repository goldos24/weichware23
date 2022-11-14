package sc.player2023.logic.mcts.evaluators;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.Statistics;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;

public class RatingWeightedUCTEvaluator implements NodeEvaluator {

    private final double explorationWeight;

    @Nonnull
    private final Rater rater;

    public RatingWeightedUCTEvaluator(double explorationWeight, @Nonnull Rater rater) {
        this.explorationWeight = explorationWeight;
        this.rater = rater;
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
        Rating rating = this.rater.rate(gameState);

        // The pure UCT value is multiplied by the rating of the child game state.
        // Therefore, paths with more promising ratings are explored and exploited.
        return uctEvaluateNode(parentNode, childNode) * rating.rating();
    }
}
