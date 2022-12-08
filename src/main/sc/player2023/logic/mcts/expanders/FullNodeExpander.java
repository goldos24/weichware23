package sc.player2023.logic.mcts.expanders;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.NodeExpansionProvider;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeExpander;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FullNodeExpander extends NodeExpander {

    public FullNodeExpander(@Nonnull NodeExpansionProvider nodeProvider) {
        super(nodeProvider);
    }

    @Nonnull
    @Override
    public List<MCTSTreeNode> createChildren(@Nonnull MCTSTreeNode selectedNode) {
        ImmutableGameState selectedGameState = selectedNode.getGameState();
        assert !selectedGameState.isOver();

        List<Move> moves = GameRuleLogic.getPossibleMoves(selectedGameState);
        int expansionAmount = moves.size();

        List<MCTSTreeNode> children = new ArrayList<>();
        for (int i = 0; i < expansionAmount; ++i) {
            int randomMoveIndex = GameRuleLogic.getRandomMoveIndex(moves);
            Move randomMove = moves.get(randomMoveIndex);

            MCTSTreeNode providedNode = this.getNodeProvider().provideNode(selectedGameState, randomMove);
            children.add(providedNode);
        }

        return children;
    }

    @Override
    public boolean canExpand(@Nonnull MCTSTreeNode node) {
        ImmutableGameState gameState = node.getGameState();
        if (gameState.isOver())
            return false;

        Stream<Move> possibleMoveStream = GameRuleLogic.getPossibleMoveStream(gameState);
        return possibleMoveStream.findFirst().isPresent();
    }
}
