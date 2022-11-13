package sc.player2023.logic.mcts;

import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.initialisers.AllMovesInitialiser;

import javax.annotation.Nonnull;

public class MCTSTreeFixture {

    public static MCTSTree createTestTree() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        return MCTSTree.fromInitialiser(gameState, initialiser);
    }

    public static MCTSTree createWithGameState(@Nonnull ImmutableGameState gameState) {
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        return MCTSTree.fromInitialiser(gameState, initialiser);
    }
}
