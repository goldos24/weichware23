package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReachableFishRaterTest {

    @Test
    void indexToCoordsToIndex() {
        List<Coordinates> expected = GameRuleLogic.createBoardCoordinateStream().toList();
        List<Coordinates> got = expected.stream().map(GameRuleLogic::coordsToIndex).
                                        map(GameRuleLogic::indexToCoords).toList();
        assertEquals(expected, got);
    }

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
    static final int REACHABLE_NORMAL_FISH_COUNT = 55;
    static final int REACHABLE_MORE_FISH_FIELD_COUNT = 1;
    static final int REACHABLE_FISH_ON_DEFAULT_BOARD = REACHABLE_MORE_FISH_FIELD_COUNT * REACHABLE_NORMAL_FISH_COUNT * BoardFixture.DEFAULT_FISH_COUNT +
            REACHABLE_MORE_FISH_FIELD_COUNT * BoardFixture.DEFAULT_MORE_FISH_COUNT;

    @Test
    void getReachableFishFromCoordinate() {
        BoardPeek testBoard = new BoardPeek(BoardFixture.createTestBoard());
        var startCoord = BoardFixture.FIRST_PENGUIN_COORDINATES;
        int got = ReachableFishRater.getReachableFishFromCoordinate(startCoord, testBoard);
        assertEquals(REACHABLE_FISH_ON_DEFAULT_BOARD, got);
    }

    @Test
    void rate() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Rater rater = new ReachableFishRater();
        Rating expected = Rating.ZERO;
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }

    @Test
    void rateInSensibleCase() {
        ImmutableGameState gameState = GameStateFixture.createReachableFishTestGameState();
        Rater rater = new ReachableFishRater();
        Rating expected = new Rating(-2);
        Rating got = rater.rate(gameState);
        assertEquals(expected, got);
    }
}
