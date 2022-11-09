package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionTest {

    private static final int EXPANSION_AMOUNT = 2;
    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    ImmutableGameState gameState;

    MCTSTree tree;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        NodeEvaluator evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.tree = new MCTSTree(this.gameState, evaluator);
    }

    @Test
    void expansionMakesNewNodeTest() {
        List<Integer> selectedNodeTrace = this.tree.select();
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
