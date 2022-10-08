package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StupidMoveGetterTest {
    private static ImmutableGameState createTestGameState() {
        GameState gameState = new GameState(createTestBoard());
        return new ImmutableGameState(gameState);
    }

    private static final int PENGUIN_COUNT = 4;
    private static final int PENGUIN_START_X = 2;
    private static final int PENGUIN_START_Y = 0;

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;
    private static final int DEFAULT_FISH_COUNT = 1;
    private static final int DEFAULT_MORE_FISH_COUNT = 3;

    private static final int DEFAULT_MORE_FISH_X = 1;
    private static final int DEFAULT_MORE_FISH_Y = 0;
    private static List<Field> createSingleBoardFieldLine() {
        List<Field> result = new ArrayList<>(BOARD_WIDTH);
        for(int i = 0; i < BOARD_WIDTH; ++i) {
            result.add(new Field(DEFAULT_FISH_COUNT, null));
        }
        return result;
    }

    private static Board createTestBoard() {
        List<List<Field>> resultingFields = new ArrayList<>(BOARD_HEIGHT);
        for(int i = 0; i < BOARD_HEIGHT; ++i) {
            resultingFields.add(createSingleBoardFieldLine());
        }
        resultingFields.get(DEFAULT_MORE_FISH_Y).set(DEFAULT_MORE_FISH_X, new Field(DEFAULT_MORE_FISH_COUNT, null));
        for(int i = 0; i < PENGUIN_COUNT; ++i) {
            resultingFields.get(PENGUIN_START_Y).set(PENGUIN_START_X+i, new Field(0, Team.ONE));
            resultingFields.get(PENGUIN_START_Y+1).set(PENGUIN_START_X+i, new Field(0, Team.TWO));
        }
        return new Board(resultingFields);
    }

    @Test
    void stupidMoveGetterStupidRaterTest() {
        ImmutableGameState testGameState = createTestGameState();
        System.out.println(testGameState.getPointsForTeam(Team.ONE));
        System.out.println(testGameState.getPointsForTeam(Team.TWO));
        MoveGetter moveGetter = new StupidMoveGetter();
        Rater rater = new StupidRater();
        Move move = moveGetter.getBestMove(testGameState, Team.ONE, rater);
        testGameState = GameRuleLogic.withMovePerformed(testGameState, move);
        assertEquals(-DEFAULT_MORE_FISH_COUNT, rater.rate(testGameState));
        assertEquals(DEFAULT_MORE_FISH_COUNT, testGameState.getPointsForTeam(Team.ONE));
    }
}