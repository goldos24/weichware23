package sc.player2023.logic.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardParserTest {
    @Test
    void boardFromString() {
        String sourceString = """
                                 1 3 G G G G 1 1\s
                                  1 1 P P P P 1 1\s
                                 1 1 1 1 1 1 1 1\s
                                  1 1 1 1 1 1 1 1\s
                                 1 1 1 1 1 1 1 1\s
                                  1 1 1 1 1 1 1 1\s
                                 1 1 1 1 1 1 1 1\s
                                  1 1 1 1 1 1 1 1\s
                                 """;
        BoardPeek expected = new BoardPeek(BoardFixture.createTestBoard());
        BoardPeek got = BoardParser.boardFromString(sourceString);
        assertEquals(expected, got);
    }
}
