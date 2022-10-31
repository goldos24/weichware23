package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Vector;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMoveIteratorTest {

    @Test
    void getPossibleTargetCoordsForPenguinInDirection() {
        BoardPeek board = new BoardPeek(BoardFixture.createTestBoard());
        Stream<Coordinates> coordStream = PossibleMoveIterator.getPossibleTargetCoordsForPenguinInDirection(
                board,
                BoardFixture.firstPenguinCoords,
                new Vector(-2, 0)
        );
        List<Coordinates> got = coordStream.toList();
        List<Coordinates> expected = List.of(new Coordinates(2, 0), new Coordinates(0,0));
        assertEquals(expected, got);
    }
}