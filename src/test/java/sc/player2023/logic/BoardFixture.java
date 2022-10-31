package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Till Fransson
 * @since 09.10.2022
 */
public class BoardFixture {
    static final int DEFAULT_MORE_FISH_COUNT = 3;
    private static final int PENGUIN_COUNT = 4;
    static final int PENGUIN_START_X = 2;
    static final int PENGUIN_START_Y = 0;
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;
    static final int DEFAULT_FISH_COUNT = 1;
    private static final int DEFAULT_MORE_FISH_X = 1;
    private static final int DEFAULT_MORE_FISH_Y = 0;

    @SuppressWarnings("all")
    private static Coordinates coordsFromXY(int x, int y) {
        return new Coordinates(x*2+y%2, y);
    }

    static final Coordinates firstPenguinCoords = coordsFromXY(PENGUIN_START_X, PENGUIN_START_Y);

    private BoardFixture() {
    }

    static Board createTestBoardOneFishPerField() {
        List<List<Field>> resultingFields = new ArrayList<>();
        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            resultingFields.add(createSingleBoardFieldLine());
        }
        return new Board(resultingFields);
    }

    private static List<Field> createSingleBoardFieldLine() {
        List<Field> result = new ArrayList<>(BOARD_WIDTH);
        for (int i = 0; i < BOARD_WIDTH; ++i) {
            result.add(new Field(DEFAULT_FISH_COUNT, null));
        }
        return result;
    }

    static Board createTestBoard() {
        List<List<Field>> resultingFields = new ArrayList<>(BOARD_HEIGHT);
        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            resultingFields.add(createSingleBoardFieldLine());
        }
        resultingFields.get(DEFAULT_MORE_FISH_Y).set(DEFAULT_MORE_FISH_X, new Field(DEFAULT_MORE_FISH_COUNT, null));
        for (int i = 0; i < PENGUIN_COUNT; ++i) {
            resultingFields.get(PENGUIN_START_Y).set(PENGUIN_START_X + i, new Field(0, Team.ONE));
            resultingFields.get(PENGUIN_START_Y + 1).set(PENGUIN_START_X + i, new Field(0, Team.TWO));
        }
        return new Board(resultingFields);
    }
}
