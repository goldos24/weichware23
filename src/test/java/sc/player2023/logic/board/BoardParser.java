package sc.player2023.logic.board;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.plugin2023.Field;

import java.util.Map;

public class BoardParser {
    private static char getCharAtCoordinateFromBoardString(Coordinates coordinates, String boardString) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        int index = (GameRuleLogic.BOARD_WIDTH*2+1)*y+y/2+x;
        return boardString.charAt(index);
    }

    private static final Map<Character, Field> CHARACTER_FIELD_MAP = Map.of(
            '-', new Field(0, null),
            'G', new Field(0, Team.ONE),
            'P', new Field(0, Team.TWO),
            '1', new Field(1, null),
            '2', new Field(2, null),
            '3', new Field(3, null),
            '4', new Field(4, null)
    );

    public static BoardPeek boardFromString(String boardString) {
        return BoardPeek.fromStreams(GameRuleLogic.createBoardCoordinateStream().
                map(coordinates -> CHARACTER_FIELD_MAP.get(getCharAtCoordinateFromBoardString(coordinates, boardString))));
    }
}
