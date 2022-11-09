package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Expansion {

    @Nonnull
    private final MCTSTreeNode selectedNode;

    public Expansion(@Nonnull MCTSTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isPossible() {
        return !this.selectedNode.getGameState().isOver();
    }

    private MCTSTreeNode createSimulatedNode(ImmutableGameState parentGameState) {
        List<Move> moves = GameRuleLogic.getPossibleMoves(parentGameState);
        int randomMoveIndex = GameRuleLogic.getRandomMoveIndex(moves);
        Move randomMove = moves.get(randomMoveIndex);
        ImmutableGameState gameStateWithRandomMove = GameRuleLogic.withMovePerformed(parentGameState, randomMove);

        Playout playout = new Playout(gameStateWithRandomMove);
        ImmutableGameState playedOutGameState = playout.complete();
        ITeam currentTeam = gameStateWithRandomMove.getCurrentTeam();
        PlayoutResult result = PlayoutResult.of(playedOutGameState, currentTeam);

        Statistics statistics = Statistics.ofPlayoutResult(result, currentTeam);
        return new MCTSTreeNode(statistics, randomMove, gameStateWithRandomMove, List.of());
    }

    @Nonnull
    public List<MCTSTreeNode> expandAndSimulate(int expansionAmount) {
        ImmutableGameState selectedGameState = this.selectedNode.getGameState();
        if (selectedGameState.isOver()) {
            return List.of();
        }

        List<MCTSTreeNode> children = new ArrayList<>();
        for (int i = 0; i < expansionAmount; ++i) {
            MCTSTreeNode simulatedNode = this.createSimulatedNode(selectedGameState);
            children.add(simulatedNode);
        }

        return children;
    }
}
