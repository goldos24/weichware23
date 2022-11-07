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

    private ImmutableMCTSTreeNode node;

    @BeforeEach
    void setUp() {
        this.node = ImmutableMCTSTreeNodeFixture.createTestNode();
    }

    @Test
    void withStatisticsTest() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Statistics statistics = StatisticsFixture.createTestStatistics();
        ImmutableMCTSTreeNode expected = new ImmutableMCTSTreeNode(statistics, null, gameState, List.of());

        ImmutableMCTSTreeNode actual = this.node.withStatistics(statistics);

        assertEquals(expected, actual);
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
        ImmutableMCTSTreeNode expected = new ImmutableMCTSTreeNode(statistics, null, gameState, List.of());

        ImmutableMCTSTreeNode actual = this.node.withPlayoutResult(playoutResult);

        assertEquals(expected, actual);
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

        ImmutableMCTSTreeNode expandedNode = new ImmutableMCTSTreeNode(playedOutGameState).withPlayoutResult(playoutResult);

        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        ImmutableMCTSTreeNode node = new ImmutableMCTSTreeNode(testGameState).withAdditionalChildren(List.of(expandedNode));
        ImmutableMCTSTreeNode backpropagatedNode = node.withBackpropagatedChildAfterSteps(List.of(0), expandedNode);

        int children = backpropagatedNode.getChildren().size();

        assertEquals(1, children);

        Statistics expectedStatistics = new Statistics(1, 0);
        assertEquals(backpropagatedNode.getStatistics(), expectedStatistics);
    }
}
