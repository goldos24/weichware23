package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.plugin2023.GameState;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Till Fransson
 * @since 08.10.2022
 */
public class GameStateFixture {


    public static final int POINTS_TEAM_TWO = 0;
    public static final int POINTS_TEAM_ONE = 5;

    public static ImmutableGameState createTestGameState() {
        GameState gameState = new GameState(BoardFixture.createTestBoard());
        return ImmutableGameStateFactory.createFromGameState(gameState);
    }

    public static ImmutableGameState createTestGameStateOneFishPerField() {
        GameState gameState = new GameState(BoardFixture.createTestBoardOneFishPerField());
        Map.Entry<Team, Integer> teamOne = entry(Team.ONE, POINTS_TEAM_ONE);
        Map.Entry<Team, Integer> teamTwo = entry(Team.TWO, POINTS_TEAM_TWO);
        ImmutableMap<ITeam, Integer> pointsMap = ImmutableMap.ofEntries(teamOne, teamTwo);
        return new ImmutableGameState(gameState, pointsMap);
    }

}
