package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Board;

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
    void testSame() {
        assertEquals(gameState, gameState, "same");
    }

    @Test
    void testDifferent() {
        assertNotEquals(gameState, "This is not a ImmutableGameState!", "different");
    }

    @Test
    void getCurrentTeam() {
        assertEquals(gameState.getCurrentTeam(), Team.ONE);
    }
}
