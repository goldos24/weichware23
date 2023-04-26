package sc.player2023.logic.board;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.plugin2023.Field;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class BoardParser {

    private BoardParser() {
    }

    private static final Map<Character, Field> CHARACTER_FIELD_MAP = Map.of(
            ' ', new Field(0, null),
            'G', new Field(0, Team.ONE),
            'P', new Field(0, Team.TWO),
            '-', new Field(1, null),
            '=', new Field(2, null),
            '3', new Field(3, null),
            '4', new Field(4, null)
    );

    @Nonnull
    public static BoardPeek boardFromString(@Nonnull String boardString) {
        Stream<Coordinates> stream = GameRuleLogic.createBoardCoordinateStream();
        Function<Coordinates, Field> getField = coordinates -> {
            char atCoordinate = getCharAtCoordinate(coordinates, boardString);
            return CHARACTER_FIELD_MAP.get(atCoordinate);
        };
        Stream<Field> fields = stream.map(getField);
        return BoardPeek.fromStreams(fields);
    }

    private static char getCharAtCoordinate(@Nonnull Coordinates coordinates,
                                            @Nonnull String boardString) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        int index = (GameRuleLogic.BOARD_WIDTH * 2 + 1) * y + y / 2 + x;
        return boardString.charAt(index);
    }
}
