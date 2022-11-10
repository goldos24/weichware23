package sc.player2023.logic.mcts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.ImmutableGameState;
import sc.plugin2023.Move;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSTreeNodeTest {

    private MCTSTreeNode node;

    @BeforeEach
    void setUp() {
        this.node = MCTSTreeNodeFixture.createTestNode();
    }

    @Test
    void withStatisticsTest() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Statistics statistics = StatisticsFixture.createTestStatistics();
        MCTSTreeNode expected = new MCTSTreeNode(statistics, null, gameState, List.of());

        this.node.setStatistics(statistics);

        assertEquals(expected, this.node);
    }

    Move firstMovePicker(ImmutableGameState gameState) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        return possibleMoves.get(0);
    }

    @Test
    void withPlayoutResultTest() {
        // Always yields the following PlayoutResult:
        // PlayoutResult { points = { TWO=25, ONE=27 }, affectedTeam=TWO, kind=LOSS }
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        assertEquals(Team.ONE, gameState.getCurrentTeam());

        ImmutableGameState playedOutGameState = GameStateFixture.createPlayedOutTestGameState(gameState, this::firstMovePicker);
        ITeam currentTeam = playedOutGameState.getCurrentTeam();
        assertEquals(Team.TWO, currentTeam);

        PlayoutResult playoutResult = PlayoutResult.of(playedOutGameState, currentTeam);
        assertEquals(PlayoutResult.Kind.LOSS, playoutResult.getKind());
        assertEquals(Team.TWO, playoutResult.getAffectedTeam());

        // The playout result is a loss for team two, and thus a win for team one
        Statistics statistics = Statistics.zeroed().addWin();
        MCTSTreeNode expected = new MCTSTreeNode(statistics, null, gameState, List.of());

        this.node.addPlayoutResult(playoutResult);

        assertEquals(expected, this.node);
    }

    @Test
    void withBackpropagatedChildTest() {
        // Always yields the following PlayoutResult:
        // PlayoutResult { points = { TWO=25, ONE=27 }, affectedTeam=TWO, kind=LOSS }
        ImmutableGameState gameState = GameStateFixture.createTestGameStateWithFirstMovePerformed();
        assertEquals(Team.TWO, gameState.getCurrentTeam());

        ImmutableGameState playedOutGameState = GameStateFixture.createPlayedOutTestGameState(gameState, this::firstMovePicker);
        ITeam currentTeam = playedOutGameState.getCurrentTeam();
        assertEquals(Team.TWO, currentTeam);

        PlayoutResult playoutResult = PlayoutResult.of(playedOutGameState, currentTeam);
        System.out.println(playoutResult);
        assertEquals(PlayoutResult.Kind.LOSS, playoutResult.getKind());
        assertEquals(Team.TWO, playoutResult.getAffectedTeam());

        MCTSTreeNode expandedNode = new MCTSTreeNode(playedOutGameState);
        expandedNode.addPlayoutResult(playoutResult);

        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        MCTSTreeNode node = new MCTSTreeNode(testGameState);
        MCTSTreeNode backpropagatedNode = node.addBackpropagatedChildrenAfterSteps(List.of(), List.of(expandedNode));

        int children = backpropagatedNode.getChildren().size();
        assertEquals(1, children);

        // The playout result is a loss for team two, and thus a win for team one
        Statistics statistics = Statistics.zeroed().addWin();
        assertEquals(statistics, backpropagatedNode.getStatistics());
    }
}
