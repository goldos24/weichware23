package sc.player2023.logic.mcts.expanders;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.mcts.*;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PlayoutNodeExpansionProvider implements NodeExpansionProvider {

    @Override
    @Nonnull
    public MCTSTreeNode provideNode(ImmutableGameState parentGameState, Move move) {
        ImmutableGameState gameStateWithRandomMove = GameRuleLogic.withMovePerformed(parentGameState, move);

        ImmutableGameState playedOutGameState = Playout.completeRandomly(gameStateWithRandomMove);
        ITeam currentTeam = gameStateWithRandomMove.getCurrentTeam();
        PlayoutResult result = PlayoutResult.of(playedOutGameState, currentTeam);

        Statistics statistics = Statistics.ofPlayoutResult(result, currentTeam);
        return new MCTSTreeNode(statistics, move, gameStateWithRandomMove, List.of());
    }
}
