package sc.player2023.logic;

import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

public class PureMCTSMoveGetter implements MoveGetter {
    private final TimeMeasurer timer = new TimeMeasurer(1900);

	/**
	 * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
	 * https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation
	 */
	private double uct(int nodeWins, int nodeVisits, int parentNodeVisits, double explorationWeight) {
		double exploitation = (double)nodeWins / (double)nodeVisits;
		double exploration = Math.log(parentNodeVisits) / nodeVisits;
		return exploitation + explorationWeight * exploration;
	}

	private double uct(int nodeWins, int nodeVisits, int parentNodeVisits) {
		return this.uct(nodeWins, nodeVisits, parentNodeVisits, Math.sqrt(2));
	}

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();
    }
}
