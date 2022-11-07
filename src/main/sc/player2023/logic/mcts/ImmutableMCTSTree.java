package sc.player2023.logic.mcts;

import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ImmutableMCTSTree {
    @Nonnull
    private final ImmutableMCTSTreeNode rootNode;

    public ImmutableMCTSTree(@Nonnull ImmutableGameState initialGameState) {
        this.rootNode = new ImmutableMCTSTreeNode(initialGameState);
    }

    public ImmutableMCTSTree(@Nonnull ImmutableMCTSTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    @Nonnull
    public ImmutableMCTSTreeNode getRootNode() {
        return this.rootNode;
    }

    @Nullable
    public Move bestMove() {
        Move bestMove = null;
        int bestWins = Integer.MIN_VALUE;

        for (ImmutableMCTSTreeNode node : this.rootNode.getChildren()) {
            Statistics nodeStatistics = node.getStatistics();

            int wins = nodeStatistics.wins();
            if (wins > bestWins) {
                bestMove = node.getMove();
                bestWins = wins;
            }
        }

        return bestMove;
    }

    @Nonnull
    public Selection select() {
        return new Selection(this.rootNode);
    }

    @Nullable
    public ImmutableMCTSTreeNode trace(List<Integer> stepsToNode) {
        if (stepsToNode.isEmpty()) {
            return this.rootNode;
        }
        return this.rootNode.trace(stepsToNode);
    }

    @Nonnull
    public Expansion expand(List<Integer> stepsToSelectedNode) {
        ImmutableMCTSTreeNode tracedNode = this.trace(stepsToSelectedNode);
        assert tracedNode != null;
        return new Expansion(tracedNode);
    }
}
