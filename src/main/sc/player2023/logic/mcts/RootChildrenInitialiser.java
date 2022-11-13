package sc.player2023.logic.mcts;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.List;

public interface RootChildrenInitialiser {
    @Nonnull
    List<MCTSTreeNode> getChildren(@Nonnull ImmutableGameState rootGameState);
}
