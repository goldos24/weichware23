package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Board;
import sc.plugin2023.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Till Fransson
 * @since 09.10.2022
 */
public class BoardFixture {
    public static final int DEFAULT_MORE_FISH_COUNT = 3;
    private static final int PENGUIN_COUNT = 4;
    public static final int PENGUIN_START_X = 2;
    public static final int PENGUIN_START_Y = 0;
    public static final int DEFAULT_FISH_COUNT = 1;
    private static final int DEFAULT_MORE_FISH_X = 1;
    private static final int DEFAULT_MORE_FISH_Y = 0;

    @SuppressWarnings("all")
    private static Coordinates coordsFromXY(int x, int y) {
        return new Coordinates(x*2+y%2, y);
    }

    public static final Coordinates firstPenguinCoords = coordsFromXY(PENGUIN_START_X, PENGUIN_START_Y);

    private BoardFixture() {
    }

    public static Board createTestBoardOneFishPerField() {
        List<List<Field>> resultingFields = new ArrayList<>();
        for (int i = 0; i < GameRuleLogic.BOARD_HEIGHT; ++i) {
            resultingFields.add(createSingleBoardFieldLine());
        }
        return new Board(resultingFields);
    }

    private static List<Field> createSingleBoardFieldLine() {
        List<Field> result = new ArrayList<>(GameRuleLogic.BOARD_WIDTH);
        for (int i = 0; i < GameRuleLogic.BOARD_WIDTH; ++i) {
            result.add(new Field(DEFAULT_FISH_COUNT, null));
        }
        return result;
    }

    public static Board createTestBoard() {
        List<List<Field>> resultingFields = new ArrayList<>(GameRuleLogic.BOARD_HEIGHT);
        for (int i = 0; i < GameRuleLogic.BOARD_HEIGHT; ++i) {
            resultingFields.add(createSingleBoardFieldLine());
        }
        resultingFields.get(DEFAULT_MORE_FISH_Y).set(DEFAULT_MORE_FISH_X, new Field(DEFAULT_MORE_FISH_COUNT, null));
        for (int i = 0; i < PENGUIN_COUNT; ++i) {
            resultingFields.get(PENGUIN_START_Y).set(PENGUIN_START_X + i, new Field(0, Team.ONE));
            resultingFields.get(PENGUIN_START_Y + 1).set(PENGUIN_START_X + i, new Field(0, Team.TWO));
        }
        return new Board(resultingFields);
    }

    public static BoardPeek createImmutableReachableFishRaterTestBoard() {
        Map<Coordinates, Field> fieldMap = Map.of(
                new Coordinates(0, 0), new Field(DEFAULT_FISH_COUNT, null),
                new Coordinates(2, 0), new Field(0, Team.ONE),
                new Coordinates(4, 0), new Field(0, Team.TWO),
                new Coordinates(6, 0), new Field(DEFAULT_MORE_FISH_COUNT, null));
                return BoardPeek.fromStreams(
                GameRuleLogic.createBoardCoordinateStream().map(
                        coordinates -> {
                            var field = fieldMap.get(coordinates);
                            return field == null ? new Field(0, null) : field;
                        }
                ));
    }
}
