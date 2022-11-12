package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionTest {

    private static final int EXPANSION_AMOUNT = 2;

    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    ImmutableGameState gameState;

    MCTSTree tree;

    NodeSelector selector;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        NodeEvaluator evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.selector = new BasicEvaluatorSelector(evaluator);
        this.tree = MCTSTree.ofGameStateWithAllChildren(gameState);
    }

    @Test
    void expansionMakesNewNodeTest() {
        MCTSTreeNode rootNode = this.tree.rootNode();
        List<Integer> selectedNodeTrace = this.selector.select(rootNode);
        Expansion expansion = this.tree.createExpansion(selectedNodeTrace);

        assertTrue(expansion.isPossible());

        List<MCTSTreeNode> expandedNodes = expansion.expandAndSimulate(EXPANSION_AMOUNT);
        assertEquals(EXPANSION_AMOUNT, expandedNodes.size());

        for (MCTSTreeNode expandedNode : expandedNodes) {
            List<MCTSTreeNode> children = expandedNode.getChildren();

            for (MCTSTreeNode child : children) {
                long visits = child.getStatistics().visits();
                assertEquals(1, visits);
            }
        }
    }
}
