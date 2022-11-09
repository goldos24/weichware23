package sc.player2023.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.mcts.*;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PureMCTSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(PureMCTSMoveGetter.class);

    private final TimeMeasurer timer = new TimeMeasurer(1700);

    // TODO: find a more suitable exploration weight for this game
    public static final double EXPLORATION_WEIGHT = Math.sqrt(2);
    public static final int EXPANSION_AMOUNT = 2;

    @Nonnull
    private final NodeEvaluator evaluator;

    public PureMCTSMoveGetter() {
        this.evaluator = new PureUCTEvaluator();
    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

        MCTSTree tree = MCTSTree.ofGameStateWithChildren(gameState, this.evaluator);

        log.info("MCTS begins");

        long totalSelectionPathLength = 0;
        long selections = 0;

        while (!timer.ranOutOfTime()) {
            List<Integer> selectedPath = tree.select();

            totalSelectionPathLength += selectedPath.size();
            selections++;

            Expansion expansion = tree.createExpansion(selectedPath);

            if (!expansion.isPossible()) {
                continue;
            }

            List<MCTSTreeNode> newNodes = expansion.expandAndSimulate(EXPANSION_AMOUNT);
            log.info("Expanded nodes: {}", newNodes.size());

            MCTSTreeNode rootNode = tree.getRootNode();
            log.info("Root stats: {}", rootNode.getStatistics());
            rootNode.addBackpropagatedChildrenAfterSteps(selectedPath, newNodes);
            log.info("Root stats with expanded node: {}", rootNode.getStatistics());

            System.out.println();
        }

        MCTSTreeNode treeRootNode = tree.getRootNode();
        Statistics treeRootNodeStatistics = treeRootNode.getStatistics();
        List<MCTSTreeNode> rootNodeChildren = treeRootNode.getChildren();

        log.info("Root node visits: {}", treeRootNodeStatistics.visits());
        log.info("Root node wins: {}", treeRootNodeStatistics.wins());
        log.info("Root node children: {}", rootNodeChildren.size());
        log.info("Average selection path length: {} with {} selections", (double)totalSelectionPathLength / selections, selections);

        Move move = tree.bestMove();
        log.info("Selected move: {}", move);
        return move;
    }
}
