package sc.player2023.logic;

import sc.player2023.logic.mcts.Expansion;
import sc.player2023.logic.mcts.ImmutableMCTSTree;
import sc.player2023.logic.mcts.ImmutableMCTSTreeNode;
import sc.player2023.logic.mcts.Selection;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PureMCTSMoveGetter implements MoveGetter {
    private final TimeMeasurer timer = new TimeMeasurer(1900);

    // TODO: find a more suitable exploration weight for this game
    public static final double EXPLORATION_WEIGHT = Math.sqrt(2);
    public static final int EXPANSION_AMOUNT = 2;

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

        ImmutableMCTSTree tree = new ImmutableMCTSTree(gameState);
        while (!timer.ranOutOfTime()) {
            Selection selection = tree.select();
            List<Integer> selectedPath = selection.complete();
            Expansion expansion = tree.expand(selectedPath);

            // Dead end, no expansion is possible
            if (!expansion.isPossible()) {
                continue;
            }

            ImmutableMCTSTreeNode expandedNode = expansion.expandAndSimulate(EXPANSION_AMOUNT);
        }

        return tree.bestMove();
    }
}
