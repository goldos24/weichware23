package sc.player2023.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.player2023.logic.mcts.ImmutableMCTSTreeNode;
import sc.player2023.logic.mcts.Selection;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionTest {

    private Selection selection;
    private ImmutableGameState gameState;

    @BeforeEach
    void setUp() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        ImmutableMCTSTreeNode rootNode = new ImmutableMCTSTreeNode(gameState);
        this.gameState = gameState;
        this.selection = new Selection(rootNode);
    }

    @Test
    void selectionReturnsInitialGameStateTest() {
        ImmutableMCTSTreeNode completed = this.selection.complete();
        ImmutableGameState completedGameState = completed.getGameState();
        assertEquals(gameState, completedGameState);
    }
}
