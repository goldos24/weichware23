package sc.player2023.logic.mcts;

import sc.player2023.logic.ImmutableGameState;

import java.util.function.Supplier;

public class SelectionFixture {

    public static Selection createTestSelection(Supplier<ImmutableGameState> gameStateSupplier) {
        ImmutableGameState gameState = gameStateSupplier.get();
        ImmutableMCTSTreeNode rootNode = new ImmutableMCTSTreeNode(gameState);

        return new Selection(rootNode);
    }
}
