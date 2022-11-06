package sc.player2023.logic;

import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ImmutableMCTSTree {
    @Nonnull
    private final List<ImmutableMCTSTreeNode> rootChildNodes;

    public ImmutableMCTSTree(@Nonnull ImmutableGameState initialGameState) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(initialGameState);
        Stream<ImmutableMCTSTreeNode> nodeStream = possibleMoves.stream().map(ImmutableMCTSTreeNode::new);
        this.rootChildNodes = nodeStream.toList();
    }

    public Move bestMove() {
        Stream<ImmutableMCTSTreeNode> childNodeStream = this.rootChildNodes.stream();

        Optional<ImmutableMCTSTreeNode> bestNode = childNodeStream.max((a, b) -> {
            int winsOfA = a.getStatistics().wins();
            int winsOfB = b.getStatistics().wins();
            return Integer.compare(winsOfA, winsOfB);
        });

        return bestNode.get().getMove();
    }
}
