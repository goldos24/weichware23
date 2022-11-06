package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class Expansion {
    @Nonnull
    private final ImmutableMCTSTreeNode selectedNode;

    public Expansion(@Nonnull ImmutableMCTSTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isPossible() {
        return !this.selectedNode.getGameState().isOver();
    }

    private ImmutableMCTSTreeNode createSimulatedNode(ImmutableGameState parentGameState) {
        List<Move> moves = GameRuleLogic.getPossibleMoves(parentGameState);
        int randomMoveIndex = GameRuleLogic.getRandomMoveIndex(moves);
        Move randomMove = moves.get(randomMoveIndex);
        ImmutableGameState gameStateWithRandomMove = GameRuleLogic.withMovePerformed(parentGameState, randomMove);

        ITeam currentTeam = gameStateWithRandomMove.getCurrentTeam();
        Playout playout = new Playout(gameStateWithRandomMove);
        ImmutableGameState playedOutGameState = playout.complete();
        PlayoutResult result = PlayoutResult.of(playedOutGameState, currentTeam);

        return new ImmutableMCTSTreeNode(randomMove, gameStateWithRandomMove).withPlayoutResult(result);
    }

    @Nullable
    public ImmutableMCTSTreeNode expandAndSimulate(int expansionAmount) {
        ImmutableGameState selectedGameState = this.selectedNode.getGameState();
        if (selectedGameState.isOver()) {
            return null;
        }

        ImmutableMCTSTreeNode expandedNode = this.selectedNode;
        for (int i = 0; i < expansionAmount; ++i ) {
            ImmutableMCTSTreeNode simulatedNode = this.createSimulatedNode(selectedGameState);
            expandedNode = expandedNode.withChild(simulatedNode);
        }
        return expandedNode;
    }
}
