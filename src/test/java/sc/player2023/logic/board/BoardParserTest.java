package sc.player2023.logic.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardParserTest {
    @Test
    void boardFromString() {
        String sourceString = """
                                 - 3 G G G G - -\s
                                  - - P P P P - -\s
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
