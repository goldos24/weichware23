package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpansionTest {

    public static final int EXPANSION_AMOUNT = 2;
    Selection selection;

    ImmutableGameState gameState;

    ImmutableMCTSTree tree;

    @BeforeEach
    void setUp() {
        this.gameState = GameStateFixture.createTestGameState();
        this.selection = SelectionFixture.createTestSelection(() -> this.gameState);
        this.tree = new ImmutableMCTSTree(this.gameState);
    }

    @Test
    void expansionMakesNewNodeTest() {
        List<Integer> selectedNodeTrace = this.selection.complete();
        Expansion expansion = tree.expand(selectedNodeTrace);

        assertTrue(expansion.isPossible());

        ImmutableMCTSTreeNode expandedNode = expansion.expandAndSimulate(EXPANSION_AMOUNT);

        List<ImmutableMCTSTreeNode> children = expandedNode.getChildren();
        assertEquals(EXPANSION_AMOUNT, children.size());

        for (ImmutableMCTSTreeNode child : children) {
            int visits = child.getStatistics().visits();
            assertEquals(1, visits);
        }
    }
}
