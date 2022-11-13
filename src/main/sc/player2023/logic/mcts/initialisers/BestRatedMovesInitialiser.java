package sc.player2023.logic.mcts.initialisers;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.RootChildrenInitialiser;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public record BestRatedMovesInitialiser(@Nonnull Rater rater, int maxInitialChildNodes) implements RootChildrenInitialiser {

    @Nonnull
    @Override
    public List<MCTSTreeNode> getChildren(@Nonnull ImmutableGameState rootGameState) {
        Stream<Move> possibleMoveStream = GameRuleLogic.getPossibleMoveStream(rootGameState);
        Stream<Move> sortedMoveStream = possibleMoveStream.sorted((moveA, moveB) -> {
            ImmutableGameState withMoveA = GameRuleLogic.withMovePerformed(rootGameState, moveA);
            ImmutableGameState withMoveB = GameRuleLogic.withMovePerformed(rootGameState, moveB);

            Rating ratingA = this.rater.rate(withMoveA);
            Rating ratingB = this.rater.rate(withMoveB);

            return ratingA.compareTo(ratingB);
        });

        Stream<Move> limitedMoveStream = sortedMoveStream.limit(this.maxInitialChildNodes);
        Stream<MCTSTreeNode> childrenStream = limitedMoveStream.map(move -> {
            ImmutableGameState gameState = GameRuleLogic.withMovePerformed(rootGameState, move);
            return new MCTSTreeNode(move, gameState);
        });

        return childrenStream.toList();
    }
}
