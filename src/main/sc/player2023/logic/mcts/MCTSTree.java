package sc.player2023.logic.mcts;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public record MCTSTree(@Nonnull MCTSTreeNode rootNode) {

    public static MCTSTree withChildren(@Nonnull ImmutableGameState initialGameState, @Nonnull List<MCTSTreeNode> children) {
        MCTSTreeNode rootNode = new MCTSTreeNode(initialGameState);
        rootNode.addChildren(children);

        return new MCTSTree(rootNode);
    }

    public static MCTSTree fromInitialiser(@Nonnull ImmutableGameState initialGameState, @Nonnull RootChildrenInitialiser childrenInitialiser) {
        List<MCTSTreeNode> children = childrenInitialiser.getChildren(initialGameState);
        return withChildren(initialGameState, children);
    }

    @Nullable
    public Move bestMove() {
        Move bestMove = null;
        long bestWins = Long.MIN_VALUE;

        for (MCTSTreeNode node : this.rootNode.getChildren()) {
            // The node statistics need to be inverted because the children of the root node
            // contain game states and results from the perspective of the opponent team.
            Statistics nodeStatistics = node.getStatistics().inverted();

            long wins = nodeStatistics.wins();
            if (wins > bestWins) {
                bestMove = node.getMove();
                bestWins = wins;
            }
        }

        return bestMove;
    }
}
