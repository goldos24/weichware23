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

    private MCTSTreeNode createSimulatedNode(ImmutableGameState parentGameState, Move randomMove) {
        ImmutableGameState gameStateWithRandomMove = GameRuleLogic.withMovePerformed(parentGameState, randomMove);

        Playout playout = new Playout(gameStateWithRandomMove);
        ImmutableGameState playedOutGameState = playout.complete();
        ITeam currentTeam = gameStateWithRandomMove.getCurrentTeam();
        PlayoutResult result = PlayoutResult.of(playedOutGameState, currentTeam);

        Statistics statistics = Statistics.ofPlayoutResult(result, currentTeam);
        return new MCTSTreeNode(statistics, randomMove, gameStateWithRandomMove, List.of());
    }

    @Nonnull
    public List<MCTSTreeNode> expandAndSimulate(int maxExpansionAmount) {
        ImmutableGameState selectedGameState = this.selectedNode.getGameState();
        if (selectedGameState.isOver()) {
            return List.of();
        }

        // Limit expansion amount to the number of possible moves
        List<Move> moves = GameRuleLogic.getPossibleMoves(selectedGameState);
        int expansionAmount = Math.min(maxExpansionAmount, moves.size());

        List<MCTSTreeNode> children = new ArrayList<>();
        for (int i = 0; i < expansionAmount; ++i) {
            int randomMoveIndex = GameRuleLogic.getRandomMoveIndex(moves);
            Move randomMove = moves.get(randomMoveIndex);

            MCTSTreeNode simulatedNode = this.createSimulatedNode(selectedGameState, randomMove);
            children.add(simulatedNode);
        }

        return children;
    }
}
