package sc.player2023.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.*;
import sc.player2023.logic.rating.*;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class MCTSMoveGetter implements MoveGetter {

    // TODO: find a more suitable exploration weight for this game
    public static final double THEORETICAL_EXPLORATION_WEIGHT = Math.sqrt(2);

    private static final Logger log = LoggerFactory.getLogger(MCTSMoveGetter.class);

    private final TimeMeasurer timer = new TimeMeasurer(1900);

    @Nonnull private final NodeSelector selector;

    @Nonnull private final RootChildrenInitialiser rootChildrenInitialiser;

    private final int maxExpansionAmount;

    public MCTSMoveGetter(@Nonnull NodeSelector selector, @Nonnull RootChildrenInitialiser rootChildrenInitialiser, int maxExpansionAmount) {
        this.selector = selector;
        this.rootChildrenInitialiser = rootChildrenInitialiser;
        this.maxExpansionAmount = maxExpansionAmount;
    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

        List<MCTSTreeNode> children = this.rootChildrenInitialiser.getChildren(gameState);
        MCTSTree tree = MCTSTree.withChildren(gameState, children);

        long totalSelectionPathLength = 0;
        long iterations = 0;

        while (!timer.ranOutOfTime()) {
            MCTSTreeNode rootNode = tree.rootNode();
            List<Integer> selectedPath = this.selector.select(rootNode);
            Expansion expansion = tree.createExpansion(selectedPath);

            if (!expansion.isPossible()) {
                continue;
            }

            totalSelectionPathLength += selectedPath.size();
            iterations++;

            List<MCTSTreeNode> newNodes = expansion.expandAndSimulate(this.maxExpansionAmount);

            rootNode.addBackpropagatedChildrenAfterSteps(selectedPath, newNodes);
        }

        MCTSTreeNode treeRootNode = tree.rootNode();
        Statistics treeRootNodeStatistics = treeRootNode.getStatistics();
        List<MCTSTreeNode> rootNodeChildren = treeRootNode.getChildren();

        log.info("Root node visits: {}", treeRootNodeStatistics.visits());

        long rootWins = treeRootNodeStatistics.wins();
        long ownWins = treeRootNodeStatistics.inverted().wins();
        log.info("Root node wins: {} -> Own wins: {}", rootWins, ownWins);
        log.info("Root node children: {}", rootNodeChildren.size());
        log.info("Average selection path length: {} with {} full iterations", (double)totalSelectionPathLength / iterations, iterations);

        Move move = tree.bestMove();
        log.info("Selected move: {}", move);
        return move;
    }
}
