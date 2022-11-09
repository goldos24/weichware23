package sc.player2023.logic.mcts;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MCTSTree {
    @Nonnull
    private final MCTSTreeNode rootNode;

    @Nonnull
    private final NodeEvaluator selectionEvaluator;

    public MCTSTree(@Nonnull ImmutableGameState initialGameState, @Nonnull NodeEvaluator selectionEvaluator) {
        this(new MCTSTreeNode(initialGameState), selectionEvaluator);
    }

    public MCTSTree(@Nonnull MCTSTreeNode rootNode, @Nonnull NodeEvaluator selectionEvaluator) {
        this.rootNode = rootNode;
        this.selectionEvaluator = selectionEvaluator;
    }

    public static MCTSTree ofGameStateWithChildren(@Nonnull ImmutableGameState initialGameState, @Nonnull NodeEvaluator selectionEvaluator) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(initialGameState);
        List<MCTSTreeNode> children = new ArrayList<>();
        for (Move move : possibleMoves) {
            ImmutableGameState gameState = GameRuleLogic.withMovePerformed(initialGameState, move);
            MCTSTreeNode node = new MCTSTreeNode(move, gameState);
            children.add(node);
        }

        MCTSTreeNode rootNode = new MCTSTreeNode(initialGameState);
        rootNode.addChildren(children);

        return new MCTSTree(rootNode, selectionEvaluator);
    }

    @Nonnull
    public MCTSTreeNode getRootNode() {
        return this.rootNode;
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

    @Nonnull
    public List<Integer> select() {
        return Selection.complete(this.rootNode, this.selectionEvaluator);
    }

    @Nonnull
    public Expansion createExpansion(List<Integer> stepsToSelectedNode) {
        MCTSTreeNode tracedNode = this.rootNode.trace(stepsToSelectedNode);
        assert tracedNode != null;
        return new Expansion(tracedNode);
    }
}
