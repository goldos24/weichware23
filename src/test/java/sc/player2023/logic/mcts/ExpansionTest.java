package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.Logic;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;
import sc.player2023.logic.mcts.expanders.PlayoutNodeExpansionProvider;
import sc.player2023.logic.mcts.expanders.SortedCappedNodeExpander;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;
import sc.player2023.logic.rating.Rater;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionTest {

    private static final int MAX_CHILD_NODES = 2;

    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    MCTSTree tree;

    NodeSelector selector;

    NodeExpander expander;

    @BeforeEach
    void setUp() {
        Rater rater = Logic.createCombinedRater();
        NodeEvaluator evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.selector = new BasicEvaluatorSelector(evaluator);
        this.expander = new SortedCappedNodeExpander(new PlayoutNodeExpansionProvider(), rater, MAX_CHILD_NODES);
        this.tree = MCTSTreeFixture.createTestTree();
    }

    @Test
    void expansionMakesNewNodeTest() {
        MCTSTreeNode rootNode = this.tree.rootNode();
        List<Integer> selectedPath = this.selector.select(rootNode);
        MCTSTreeNode expandedNode = rootNode.trace(selectedPath);

        assertNotNull(expandedNode);
        assertTrue(this.expander.canExpand(expandedNode));

        List<MCTSTreeNode> expandedNodes = this.expander.createChildren(expandedNode);
        assertEquals(MAX_CHILD_NODES, expandedNodes.size());

        for (MCTSTreeNode childNode : expandedNodes) {
            List<MCTSTreeNode> children = childNode.getChildren();

            for (MCTSTreeNode child : children) {
                long visits = child.getStatistics().visits();
                assertEquals(1, visits);
            }
        }
    }
}
