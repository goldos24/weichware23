package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.player2023.logic.BoardFixture;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReachableFishRaterTest {

    static int compareCoords(Coordinates a, Coordinates b) {
        if(a.getX() == b.getX()) {
            return Integer.compare(a.getY(), b.getY());
        }
        return Integer.compare(a.getX(), b.getX());
    }

    @Test
    void getReachableCoordsFromCoordinate() {
        BoardPeek testBoard = new BoardPeek(BoardFixture.createTestBoardOneFishPerField());
        Coordinates testStartCoord = new Coordinates(5,5);
        List<Coordinates> expected = new ArrayList<>(GameRuleLogic.createBoardCoordinateStream().toList());
        expected.sort(ReachableFishRaterTest::compareCoords);
        List<Coordinates> got = new ArrayList<>(ReachableFishRater.getReachableCoordsFromCoordinate(testStartCoord, testBoard));
        got.sort(ReachableFishRaterTest::compareCoords);
        assertEquals(expected, got);
    }

    @Test
    void getReachableFishFromCoordinate() {
        BoardPeek testBoard = new BoardPeek(BoardFixture.createTestBoard());
        var startCoord = BoardFixture.firstPenguinCoords;
        final int REACHABLE_NORMAL_FISH_COUNT = 55;
        final int REACHABLE_MORE_FISH_FIELD_COUNT = 1;
        int expected = REACHABLE_MORE_FISH_FIELD_COUNT * REACHABLE_NORMAL_FISH_COUNT * BoardFixture.DEFAULT_FISH_COUNT +
                REACHABLE_MORE_FISH_FIELD_COUNT * BoardFixture.DEFAULT_MORE_FISH_COUNT;
        int got = ReachableFishRater.getReachableFishFromCoordinate(startCoord, testBoard);
        assertEquals(expected, got);
    }
}