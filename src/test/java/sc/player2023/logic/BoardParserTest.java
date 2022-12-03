package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.board.BoardPeek;
import static org.junit.jupiter.api.Assertions.*;

public class BoardParserTest {
    @Test
    void boardFromString() {
        String sourceString = """
                                 - 3 R R R R - -\s
                                  - - B B B B - -\s
                                 - - - - - - - -\s
                                  - - - - - - - -\s
                                 - - - - - - - -\s
                                  - - - - - - - -\s
                                 - - - - - - - -\s
                                  - - - - - - - -\s
                                 """;
        BoardPeek expected = new BoardPeek(BoardFixture.createTestBoard());
        BoardPeek got = BoardParser.boardFromString(sourceString);
        assertEquals(expected, got);
    }
}
