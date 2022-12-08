package sc.player2023.logic.mcts.expanders;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.NodeExpansionProvider;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeExpander;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public class FullNodeExpander extends NodeExpander {

    public FullNodeExpander(@Nonnull NodeExpansionProvider nodeProvider) {
        super(nodeProvider);
    }

    @Nonnull
    @Override
    public List<MCTSTreeNode> createChildren(@Nonnull MCTSTreeNode node) {
        ImmutableGameState selectedGameState = node.getGameState();
        assert !selectedGameState.isOver();

        List<Move> moves = GameRuleLogic.getPossibleMoves(selectedGameState);
        Stream<Move> movesStream = moves.stream();

        NodeExpansionProvider nodeProvider = this.getNodeProvider();
        Stream<MCTSTreeNode> nodesStream = movesStream.map(move -> nodeProvider.provideNode(selectedGameState, move));

        return nodesStream.toList();
    }

    @Override
    public boolean canExpand(@Nonnull MCTSTreeNode node) {
        ImmutableGameState gameState = node.getGameState();
        return !gameState.isOver();
    }
}
