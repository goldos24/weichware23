package sc.player2023.logic.mcts;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public record MCTSTree(@Nonnull MCTSTreeNode rootNode) {

    public static MCTSTree ofGameStateWithAllChildren(@Nonnull ImmutableGameState initialGameState) {
        Stream<Move> possibleMoveStream = GameRuleLogic.getPossibleMoveStream(initialGameState);
        Stream<MCTSTreeNode> childrenStream = possibleMoveStream.map(move -> {
            ImmutableGameState gameState = GameRuleLogic.withMovePerformed(initialGameState, move);
            return new MCTSTreeNode(move, gameState);
        });

        List<MCTSTreeNode> children = childrenStream.toList();
        MCTSTreeNode rootNode = new MCTSTreeNode(initialGameState);
        rootNode.addChildren(children);

        return new MCTSTree(rootNode);
    }

    public static MCTSTree ofGameStateWithChildren(@Nonnull ImmutableGameState initialGameState, @Nonnull Rater childRater, int maxChildNodes) {
        Stream<Move> possibleMoveStream = GameRuleLogic.getPossibleMoveStream(initialGameState);
        Stream<Move> sortedMoveStream = possibleMoveStream.sorted((moveA, moveB) -> {
            ImmutableGameState withMoveA = GameRuleLogic.withMovePerformed(initialGameState, moveA);
            ImmutableGameState withMoveB = GameRuleLogic.withMovePerformed(initialGameState, moveB);

            Rating ratingA = childRater.rate(withMoveA);
            Rating ratingB = childRater.rate(withMoveB);

            return ratingA.compareTo(ratingB);
        });

        Stream<Move> limitedMoveStream = sortedMoveStream.limit(maxChildNodes);
        Stream<MCTSTreeNode> childrenStream = limitedMoveStream.map(move -> {
            ImmutableGameState gameState = GameRuleLogic.withMovePerformed(initialGameState, move);
            return new MCTSTreeNode(move, gameState);
        });

        List<MCTSTreeNode> children = childrenStream.toList();
        MCTSTreeNode rootNode = new MCTSTreeNode(initialGameState);
        rootNode.addChildren(children);

        return new MCTSTree(rootNode);
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
    public Expansion createExpansion(List<Integer> stepsToSelectedNode) {
        MCTSTreeNode tracedNode = this.rootNode.trace(stepsToSelectedNode);
        assert tracedNode != null;
        return new Expansion(tracedNode);
    }
}
