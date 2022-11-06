package sc.player2023.logic.mcts;

import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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

    /**
     * Traces a node in the MCTS tree structure using the list of indices provided.
     * <br>
     * For each entry in the list, this function advances layer by layer until all
     * indices are exhausted or until the last node has no children.
     *
     * @param stepsToNode The list of indices to trace the node in the tree structure
     * @return The node, at depth = length of stepsToNode
     */
    @Nullable
    public ImmutableMCTSTreeNode trace(List<Integer> stepsToNode) {
        ImmutableMCTSTreeNode currentNode = this.rootNode;
        for (Integer index : stepsToNode) {
            List<ImmutableMCTSTreeNode> children = currentNode.getChildren();

            if (index > children.size()) {
                return null;
            }

            currentNode = children.get(index);
        }
        return currentNode;
    }

    @Nonnull
    public Expansion expand(List<Integer> stepsToSelectedNode) {
        ImmutableMCTSTreeNode tracedNode = this.trace(stepsToSelectedNode);
        assert tracedNode != null;
        return new Expansion(tracedNode);
    }
}
