package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private Selection selection;
    private ImmutableGameState gameState;

    private ImmutableMCTSTree tree;

    @BeforeEach
    void setUp() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        ImmutableMCTSTreeNode rootNode = new ImmutableMCTSTreeNode(gameState);
        this.gameState = gameState;
        this.selection = new Selection(rootNode);
        this.tree = new ImmutableMCTSTree(this.gameState);
    }

    @Test
    void selectionYieldsRootNodeTest() {
        List<Integer> selectedNodeTrace = this.selection.complete();
        assertEquals(0, selectedNodeTrace.size());

        ImmutableMCTSTreeNode selectedNode = this.tree.trace(selectedNodeTrace);
        assertNotNull(selectedNode);

        ImmutableGameState completedGameState = selectedNode.getGameState();
        assertEquals(gameState, completedGameState);
    }
}
