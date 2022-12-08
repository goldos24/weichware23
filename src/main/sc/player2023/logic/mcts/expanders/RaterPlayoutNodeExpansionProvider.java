package sc.player2023.logic.mcts.expanders;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.*;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class RaterPlayoutNodeExpansionProvider implements NodeExpansionProvider {
    @Nonnull
    private final Rater rater;

    public RaterPlayoutNodeExpansionProvider(@Nonnull Rater rater) {
        this.rater = rater;
    }

    @Override
    @Nonnull
    public MCTSTreeNode provideNode(ImmutableGameState parentGameState, Move move) {
        ImmutableGameState gameStateWithRandomMove = GameRuleLogic.withMovePerformed(parentGameState, move);

        ImmutableGameState playedOutGameState = Playout.completeByRater(gameStateWithRandomMove, this.rater);
        ITeam currentTeam = gameStateWithRandomMove.getCurrentTeam();
        PlayoutResult result = PlayoutResult.of(playedOutGameState, currentTeam);

        Statistics statistics = Statistics.ofPlayoutResult(result, currentTeam);
        return new MCTSTreeNode(statistics, move, gameStateWithRandomMove, List.of());
    }
}
