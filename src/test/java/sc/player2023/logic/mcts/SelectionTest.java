package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.ITeam;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    ImmutableGameState gameState;

    MCTSTree tree;

    NodeSelector selector;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        NodeEvaluator evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.selector = new BasicEvaluatorSelector(evaluator);
        this.tree = MCTSTreeFixture.createWithGameState(this.gameState);
    }

    @Test
    void selectionYieldsRootNodeTest() {
        MCTSTreeNode rootNode = this.tree.rootNode();
        List<Integer> selectedNodeTrace = this.selector.select(rootNode);
        assertEquals(1, selectedNodeTrace.size());

        assertEquals(rootNode.getChildren().size(), 8);

        MCTSTreeNode selectedNode = rootNode.trace(selectedNodeTrace);
        assertNotNull(selectedNode);

        ImmutableGameState completedGameState = selectedNode.getGameState();
        ITeam currentTeam = completedGameState.getCurrentTeam();
        assertEquals(currentTeam.opponent(), gameState.getCurrentTeam());
    }
}
