package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.plugin2023.Field;

import static org.junit.jupiter.api.Assertions.*;

class BoardPeekTest {

    @Test
    void get() {
        BoardPeek board = new BoardPeek(BoardFixture.createTestBoard());
        Field field = board.get(new Coordinates(0, 0));
        assertEquals(field.getFish(), BoardFixture.DEFAULT_FISH_COUNT);
    }
}