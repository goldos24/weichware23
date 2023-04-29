package sc.player2023.logic.nnet.data;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BoardConverterTest {

    @Test
    void testConvertGameState() {
        String boardString = """
            =   4 3 = 3 = -\s
             - - = 3 - = G 3\s
            = - - - P =   -\s
             - - P =   P -  \s
              - G - = - P -\s
             - - = - G - - =\s
            3 = = - 3 = - G\s
             - = 3 = 3 4   =\s
                            
            """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        BoardConverter converter = new BoardConverter();
        double[] expected = new double[]{
            0.5,  0.5, /**/ 0.0,  0.5, /**/ 1.0,  0.5, /**/ 0.75, 0.5, /**/ 0.5,  0.5, /**/ 0.75, 0.5, /**/ 0.5,  0.5, /**/ 0.25, 0.5,
            0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.5,  0.5, /**/ 0.75, 0.5, /**/ 0.25, 0.5, /**/ 0.5,  0.5, /**/ 0.0,  0.0, /**/ 0.75, 0.5,
            0.5,  0.5, /**/ 0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.0,  1.0, /**/ 0.5,  0.5, /**/ 0.0,  0.5, /**/ 0.25, 0.5,
            0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.0,  1.0, /**/ 0.5,  0.5, /**/ 0.0,  0.5, /**/ 0.0,  1.0, /**/ 0.25, 0.5, /**/ 0.0,  0.5,
            0.0,  0.5, /**/ 0.25, 0.5, /**/ 0.0,  0.0, /**/ 0.25, 0.5, /**/ 0.5,  0.5, /**/ 0.25, 0.5, /**/ 0.0,  1.0, /**/ 0.25, 0.5,
            0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.5,  0.5, /**/ 0.25, 0.5, /**/ 0.0,  0.0, /**/ 0.25, 0.5, /**/ 0.25, 0.5, /**/ 0.5,  0.5,
            0.75, 0.5, /**/ 0.5,  0.5, /**/ 0.5,  0.5, /**/ 0.25, 0.5, /**/ 0.75, 0.5, /**/ 0.5,  0.5, /**/ 0.25, 0.5, /**/ 0.0,  0.0,
            0.25, 0.5, /**/ 0.5,  0.5, /**/ 0.75, 0.5, /**/ 0.5,  0.5, /**/ 0.75, 0.5, /**/ 1.0,  0.5, /**/ 0.0,  0.5, /**/ 0.5,  0.5,
        };
        double[] actual = converter.convert(board);
        assertArrayEquals(expected, actual);
    }
}
