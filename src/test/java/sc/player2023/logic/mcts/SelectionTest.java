package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.ITeam;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    ImmutableGameState gameState;

    MCTSTree tree;

    NodeEvaluator evaluator;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        this.evaluator = new PureUCTEvaluator(EXPLORATION_WEIGHT);
        this.tree = MCTSTree.ofGameStateWithChildren(gameState);
    }

    @Test
    void selectionYieldsRootNodeTest() {
        List<Integer> selectedNodeTrace = this.tree.select(evaluator);
        assertEquals(1, selectedNodeTrace.size());

        MCTSTreeNode rootNode = this.tree.getRootNode();
        assertEquals(rootNode.getChildren().size(), 8);

        MCTSTreeNode selectedNode = rootNode.trace(selectedNodeTrace);
        assertNotNull(selectedNode);

        ImmutableGameState completedGameState = selectedNode.getGameState();
        ITeam currentTeam = completedGameState.getCurrentTeam();
        assertEquals(currentTeam.opponent(), gameState.getCurrentTeam());
    }
}
