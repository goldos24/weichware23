package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.plugin2023.Game;
import sc.plugin2023.GameState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 09.10.2022
 */
class ImmutableGameStateTest {

    private ImmutableGameState gameState = GameStateFixture.createTestGameStateOneFishPerField();

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

}
