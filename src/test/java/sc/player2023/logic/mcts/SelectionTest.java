package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private ImmutableGameState gameState;

    private MCTSTree tree;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        this.tree = new MCTSTree(this.gameState);
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
