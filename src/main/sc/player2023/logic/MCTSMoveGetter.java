package sc.player2023.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.mcts.*;
import sc.player2023.logic.rating.*;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class MCTSMoveGetter implements MoveGetter {

    // TODO: find a more suitable exploration weight for this game
    public static final double THEORETICAL_EXPLORATION_WEIGHT = Math.sqrt(2);

    public static final int EXPANSION_AMOUNT = 3;

    private static final Logger log = LoggerFactory.getLogger(MCTSMoveGetter.class);

    private final TimeMeasurer timer = new TimeMeasurer(1900);

    @Nonnull
    private final NodeSelector selector;

    public MCTSMoveGetter(@Nonnull NodeSelector selector) {
        this.selector = selector;
    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

        MCTSTree tree = MCTSTree.ofGameStateWithChildren(gameState);

        long totalSelectionPathLength = 0;
        long iterations = 0;

        while (!timer.ranOutOfTime()) {
            MCTSTreeNode rootNode = tree.getRootNode();
            List<Integer> selectedPath = this.selector.select(rootNode);
            Expansion expansion = tree.createExpansion(selectedPath);

            if (!expansion.isPossible()) {
                continue;
            }

            totalSelectionPathLength += selectedPath.size();
            iterations++;

            List<MCTSTreeNode> newNodes = expansion.expandAndSimulate(EXPANSION_AMOUNT);

            rootNode.addBackpropagatedChildrenAfterSteps(selectedPath, newNodes);
        }

        MCTSTreeNode treeRootNode = tree.getRootNode();
        Statistics treeRootNodeStatistics = treeRootNode.getStatistics();
        List<MCTSTreeNode> rootNodeChildren = treeRootNode.getChildren();

        log.info("Root node visits: {}", treeRootNodeStatistics.visits());
        log.info("Root node wins: {}", treeRootNodeStatistics.wins());
        log.info("Root node children: {}", rootNodeChildren.size());
        log.info("Average selection path length: {} with {} full iterations", (double)totalSelectionPathLength / iterations, iterations);

        Move move = tree.bestMove();
        log.info("Selected move: {}", move);
        return move;
    }
}
