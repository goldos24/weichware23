package sc.player2023.logic;

import sc.plugin2023.GameState;

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
        Integer[] pointsMap = new Integer[] {POINTS_TEAM_ONE, POINTS_TEAM_TWO};
        return new ImmutableGameState(gameState, pointsMap);
    }

}
