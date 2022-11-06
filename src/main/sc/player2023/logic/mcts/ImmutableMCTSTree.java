package sc.player2023.logic.mcts;

import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

public class ImmutableMCTSTree {
    @Nonnull
    private final ImmutableMCTSTreeNode rootNode;

    public ImmutableMCTSTree(@Nonnull ImmutableGameState initialGameState) {
        this.rootNode = new ImmutableMCTSTreeNode(initialGameState);
    }

    @Nullable
    public Move bestMove() {
        Stream<ImmutableMCTSTreeNode> childNodeStream = this.rootNode.getChildren().stream();

        Optional<ImmutableMCTSTreeNode> bestNode = childNodeStream.max((a, b) -> {
            int winsOfA = a.getStatistics().wins();
            int winsOfB = b.getStatistics().wins();
            return Integer.compare(winsOfA, winsOfB);
        });

        if (bestNode.isEmpty()) {
            return null;
        }

        return bestNode.get().getMove();
    }

    @Nonnull
    public Selection select() {
        return new Selection(this.rootNode);
    }
}
