package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.GameState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 09.10.2022
 */
class ImmutableGameStateTest {

    private final ImmutableGameState gameState = GameStateFixture.createTestGameStateOneFishPerField();

    @Test
    void getBoard() {
        Board expectedBoard = BoardFixture.createTestBoardOneFishPerField();
        BoardPeek expected = new BoardPeek(expectedBoard);
        BoardPeek actual = gameState.getBoard();
        assertEquals(expected, actual, "getBoard");
    }

    @Test
    void getPointsForTeamOne() {
        int actual = gameState.getPointsForTeam(Team.ONE);
        assertEquals(GameStateFixture.POINTS_TEAM_ONE, actual, "getPointsForTeamOne");
    }

    @Test
    void getPointsForTeamTwo() {
        int actual = gameState.getPointsForTeam(Team.TWO);
        assertEquals(GameStateFixture.POINTS_TEAM_TWO, actual, "getPointsForTeamOne");
    }

    @Test
    void testEquals() {
        GameState mutableGameState = gameState.getGameState();
        ImmutableMap<ITeam, Integer> teamPointsMap = gameState.getTeamPointsMap();
        ImmutableGameState expected = new ImmutableGameState(mutableGameState, teamPointsMap);
        assertEquals(expected, gameState, "same");
    }

    @Test
    void testSame() {
        assertEquals(gameState, gameState, "same");
    }

    @Test
    void testDifferent() {
        assertNotEquals(gameState, "This is not a ImmutableGameState!", "different");
    }

    @Test
    void testHashCode() {
        GameState mutableGameState = gameState.getGameState();
        ImmutableMap<ITeam, Integer> teamPointsMap = gameState.getTeamPointsMap();
        ImmutableGameState expectedGameState = new ImmutableGameState(mutableGameState, teamPointsMap);
        int expected = expectedGameState.hashCode();
        int actual = gameState.hashCode();
        assertEquals(expected, actual, "same");
    }

    @Test
    void testToString() {
        GameState mutableGameState = gameState.getGameState();
        ImmutableMap<ITeam, Integer> teamPointsMap = gameState.getTeamPointsMap();
        ImmutableGameState expectedGameState = new ImmutableGameState(mutableGameState, teamPointsMap);
        String expected = expectedGameState.toString();
        String actual = gameState.toString();
        assertEquals(expected, actual, "same");
    }
}
