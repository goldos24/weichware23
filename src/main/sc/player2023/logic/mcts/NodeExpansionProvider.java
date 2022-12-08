package sc.player2023.logic.mcts;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Move;

public interface NodeExpansionProvider {
    MCTSTreeNode provideNode(ImmutableGameState parentGameState, Move move);
}
