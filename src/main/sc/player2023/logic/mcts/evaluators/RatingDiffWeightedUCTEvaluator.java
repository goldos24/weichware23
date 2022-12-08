package sc.player2023.logic.mcts.evaluators;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.Statistics;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;

public class RatingDiffWeightedUCTEvaluator implements NodeEvaluator {

    private final double explorationWeight;

    @Nonnull
    private final Rater rater;

    public RatingDiffWeightedUCTEvaluator(double explorationWeight, @Nonnull Rater rater) {
        this.explorationWeight = explorationWeight;
        this.rater = rater;
    }

    /**
     * Slightly altered implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * <a href="https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation">Monte Carlo tree search#Exploration and exploitation</a>
     *
     */
    private double uctEvaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode, double explorationWeightChange) {
        long parentNodeVisits = parentNode.getStatistics().visits();

        Statistics statistics = childNode.getStatistics();
        double nodeWins = statistics.wins();
        double nodeVisits = statistics.visits();

        double exploitation = nodeWins / nodeVisits;
        double exploration  = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + (this.explorationWeight + explorationWeightChange) * Math.sqrt(exploration);
    }

    @Nonnull
    Rating calculateRatingDiffBetweenNodes(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        ImmutableGameState parentGameState = parentNode.getGameState();
        Rating parentRating = this.rater.rate(parentGameState);
        ImmutableGameState gameState = childNode.getGameState();
        Rating rating = this.rater.rate(gameState);

        return parentRating.subtract(rating);
    }

    @Override
    public double evaluateNode(MCTSTreeNode parentNode, MCTSTreeNode childNode) {
        Rating ratingDiff = this.calculateRatingDiffBetweenNodes(parentNode, childNode);
        return uctEvaluateNode(parentNode, childNode, ratingDiff.rating());
    }
}
