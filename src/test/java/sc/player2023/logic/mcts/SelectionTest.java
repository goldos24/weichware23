package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private ImmutableGameState gameState;
    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    private MCTSTree tree;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        NodeEvaluator evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.tree = new MCTSTree(this.gameState, evaluator);
    }

    @Test
    void selectionYieldsRootNodeTest() {
        List<Integer> selectedNodeTrace = this.tree.select();
        assertEquals(0, selectedNodeTrace.size());

        MCTSTreeNode rootNode = this.tree.getRootNode();
        MCTSTreeNode selectedNode = rootNode.trace(selectedNodeTrace);
        assertNotNull(selectedNode);

        ImmutableGameState completedGameState = selectedNode.getGameState();
        assertEquals(gameState, completedGameState);
    }
}
