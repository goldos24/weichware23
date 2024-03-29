package sc.player2023.logic.mcts;

import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

public class MCTSTreeNodeFixture {

    public static MCTSTreeNode createTestNode() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        return new MCTSTreeNode(gameState);
    }
}
