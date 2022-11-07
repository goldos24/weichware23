package sc.player2023.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.mcts.Expansion;
import sc.player2023.logic.mcts.ImmutableMCTSTree;
import sc.player2023.logic.mcts.ImmutableMCTSTreeNode;
import sc.player2023.logic.mcts.Selection;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PureMCTSMoveGetter implements MoveGetter {
    private static final Logger log = LoggerFactory.getLogger(PureMCTSMoveGetter.class);

    private final TimeMeasurer timer = new TimeMeasurer(1000);

    // TODO: find a more suitable exploration weight for this game
    public static final double EXPLORATION_WEIGHT = Math.sqrt(2);
    public static final int EXPANSION_AMOUNT = 1;

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

        ImmutableMCTSTree tree = new ImmutableMCTSTree(gameState);

        log.info("MCTS begins");

        long totalSelectionPathLength = 0;
        long selections = 0;

        while (!timer.ranOutOfTime()) {
            Selection selection = tree.select();
            List<Integer> selectedPath = selection.complete();

            totalSelectionPathLength += selectedPath.size();
            selections++;

            Expansion expansion = tree.expand(selectedPath);

            // Dead end, no expansion is possible
            if (!expansion.isPossible()) {
                continue;
            }

            // The selected node had a terminal game state -> do nothing
            ImmutableMCTSTreeNode expandedNode = expansion.expandAndSimulate(EXPANSION_AMOUNT);

            // If the selected path is empty, the expanded node becomes the new root node
            if (selectedPath.isEmpty()) {
                tree = new ImmutableMCTSTree(expandedNode);
                continue;
            }

            // If the selected path is not empty, it must be inserted into the root node
            ImmutableMCTSTreeNode rootNode = tree.getRootNode();
            ImmutableMCTSTreeNode rootNodeWithExpandedNode = rootNode.withBackpropagatedChildAfterSteps(selectedPath, expandedNode);
            tree = new ImmutableMCTSTree(rootNodeWithExpandedNode);
        }

        ImmutableMCTSTreeNode treeRootNode = tree.getRootNode();
        log.info("Root node visits: {}", treeRootNode.getStatistics().visits());
        log.info("Root node wins: {}", treeRootNode.getStatistics().wins());
        log.info("Average selection path length: {}", (double)totalSelectionPathLength / selections);

        Move move = tree.bestMove();
        log.info("Selected move: {}", move);
        return move;
    }
}
