package sc.player2023.logic.mcts.initialisers;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.RootChildrenInitialiser;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public class AllMovesInitialiser implements RootChildrenInitialiser {

    @Nonnull
    @Override
    public List<MCTSTreeNode> getChildren(@Nonnull ImmutableGameState rootGameState) {
        Stream<Move> possibleMoveStream = GameRuleLogic.getPossibleMoveStream(rootGameState);
        Stream<MCTSTreeNode> childrenStream = possibleMoveStream.map(move -> {
            ImmutableGameState gameState = GameRuleLogic.withMovePerformed(rootGameState, move);
            return new MCTSTreeNode(move, gameState);
        });

        return childrenStream.toList();
    }
}
