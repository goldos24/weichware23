package sc.player2023.logic.mcts.expanders;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.MCTSTreeNode;
import sc.player2023.logic.mcts.NodeExpander;
import sc.player2023.logic.mcts.NodeExpansionProvider;
import sc.player2023.logic.move.MoveUtil;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SortedCappedNodeExpander extends NodeExpander {

    @Nonnull
    private final Rater rater;

    private final int maxChildNodes;

    public SortedCappedNodeExpander(@Nonnull NodeExpansionProvider nodeProvider, @Nonnull Rater rater, int maxChildNodes) {
        super(nodeProvider);
        this.rater = rater;
        this.maxChildNodes = maxChildNodes;
    }

    @Nonnull
    @Override
    public List<MCTSTreeNode> createChildren(@Nonnull MCTSTreeNode node) {
        ImmutableGameState selectedGameState = node.getGameState();
        assert !selectedGameState.isOver();

        List<Move> sortedMoves = MoveUtil.sortPossibleMoves(selectedGameState, this.rater);
        int expansionAmount = Math.min(this.maxChildNodes, sortedMoves.size());

        List<MCTSTreeNode> children = new ArrayList<>();
        for (int i = 0; i < expansionAmount; ++i) {
            Move randomMove = sortedMoves.get(i);

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
