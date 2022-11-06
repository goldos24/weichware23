package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.player2023.logic.BoardFixture;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ReachableFishRaterTest {

    @Test
    void getReachableCoordsFromCoordinate() {
        BoardPeek testBoard = new BoardPeek(BoardFixture.createTestBoardOneFishPerField());
        Coordinates testStartCoord = new Coordinates(5,5);
        Set<Coordinates> expected = GameRuleLogic.createBoardCoordinateStream().collect(Collectors.toSet());
        Set<Coordinates> got = ReachableFishRater.getReachableCoordsFromCoordinate(testStartCoord, testBoard);
        assertEquals(expected, got);
    }
}