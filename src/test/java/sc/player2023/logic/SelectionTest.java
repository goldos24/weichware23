package sc.player2023.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.mcts.ImmutableMCTSTree;
import sc.player2023.logic.mcts.ImmutableMCTSTreeNode;
import sc.player2023.logic.mcts.Selection;

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
    void selectionReturnsInitialGameStateTest() {
        List<Integer> selectedNodeTrace = this.selection.complete();
        assertEquals(selectedNodeTrace.size(), 0);

        ImmutableMCTSTreeNode selectedNode = this.tree.trace(selectedNodeTrace);
        assertNotNull(selectedNode);

        ImmutableGameState completedGameState = selectedNode.getGameState();
        assertEquals(gameState, completedGameState);
    }
}
