package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardPeek;
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
            ' ', new Field(0, null),
            'R', new Field(0, Team.ONE),
            'B', new Field(0, Team.TWO),
            '-', new Field(1, null),
            '=', new Field(2, null),
            '3', new Field(3, null),
            '4', new Field(4, null)
    );

    public static BoardPeek boardFromString(String boardString) {
        return BoardPeek.fromStreams(GameRuleLogic.createBoardCoordinateStream().
                map(coordinates -> CHARACTER_FIELD_MAP.get(getCharAtCoordinateFromBoardString(coordinates, boardString))));
    }

    public static void main(String[] args) {
        System.out.println(new BoardPeek(BoardFixture.createTestBoard()));
    }
}
