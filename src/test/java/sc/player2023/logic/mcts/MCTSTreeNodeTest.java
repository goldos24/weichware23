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

public class ImmutableMCTSTreeNodeTest {

    private MCTSTreeNode node;

    @BeforeEach
    void setUp() {
        this.node = ImmutableMCTSTreeNodeFixture.createTestNode();
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
        // PlayoutResult { points = { TWO=7, ONE=4 }, affectedTeam=TWO, kind=WIN }
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        assertEquals(Team.ONE, gameState.getCurrentTeam());

        ImmutableGameState playedOutGameState = GameStateFixture.createPlayedOutTestGameState(gameState, this::firstMovePicker);
        ITeam currentTeam = playedOutGameState.getCurrentTeam();
        assertEquals(Team.TWO, currentTeam);

        PlayoutResult playoutResult = PlayoutResult.of(playedOutGameState, currentTeam);
        assertEquals(PlayoutResult.Kind.WIN, playoutResult.getKind());
        assertEquals(Team.TWO, playoutResult.getAffectedTeam());

        // The playout result is a win for team two but the current team is team one.
        Statistics statistics = Statistics.zeroed().addLossOrDraw();
        MCTSTreeNode expected = new MCTSTreeNode(statistics, null, gameState, List.of());

        this.node.addPlayoutResult(playoutResult);

        assertEquals(expected, this.node);
    }

    @Test
    void withBackpropagatedChildTest() {
        // Always yields the following PlayoutResult:
        // PlayoutResult { points = { TWO=7, ONE=4 }, affectedTeam=TWO, kind=WIN }
        ImmutableGameState gameState = GameStateFixture.createTestGameStateWithFirstMovePerformed();
        assertEquals(Team.TWO, gameState.getCurrentTeam());

        ImmutableGameState playedOutGameState = GameStateFixture.createPlayedOutTestGameState(gameState, this::firstMovePicker);
        ITeam currentTeam = playedOutGameState.getCurrentTeam();
        assertEquals(Team.TWO, currentTeam);

        PlayoutResult playoutResult = PlayoutResult.of(playedOutGameState, currentTeam);
        assertEquals(PlayoutResult.Kind.WIN, playoutResult.getKind());
        assertEquals(Team.TWO, playoutResult.getAffectedTeam());

        MCTSTreeNode expandedNode = new MCTSTreeNode(playedOutGameState);
        expandedNode.addPlayoutResult(playoutResult);

        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        MCTSTreeNode node = new MCTSTreeNode(testGameState);
        node.addChildren(List.of(expandedNode));
        MCTSTreeNode backpropagatedNode = node.addBackpropagatedChildAfterSteps(List.of(0), expandedNode);

        int children = backpropagatedNode.getChildren().size();

        assertEquals(1, children);

        Statistics expectedStatistics = new Statistics(1, 0);
        assertEquals(backpropagatedNode.getStatistics(), expectedStatistics);
    }
}
