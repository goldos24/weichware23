package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Board;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class QuadrantOccupationRaterTest {
    static Coordinates[] coords = {
            new Coordinates(3, 1),
            new Coordinates(12, 2),
            new Coordinates(4, 6),
            new Coordinates(13, 5)};

    @Test
    void getQuadrantIndex() {
        int[] expectedIndices = {0, 1, 2, 3};
        int[] got = Arrays.stream(coords).mapToInt(QuadrantOccupationRater::getQuadrantIndex).toArray();
        assertArrayEquals(expectedIndices, got);
    }

    static BoardPeek withThreeToOneOccupiedQuadrantsAdded(Board initialBoard) {
        Board boardToBeModified = initialBoard.clone();
        int index = 0;
        for(; index <3; ++index) {
            boardToBeModified.set(coords[index], Team.ONE);
        }
        for(; index < 4; ++index) {
            boardToBeModified.set(coords[index], Team.TWO);
        }
        return new BoardPeek(boardToBeModified);
    }

    static ImmutableGameState gameState = new ImmutableGameState(
            new Integer[] {0, 0},
            withThreeToOneOccupiedQuadrantsAdded(BoardFixture.createTestBoardOneFishPerField()),
            Team.ONE
    );

    @Test
    void getRatingForTeam() {
        assertEquals(new Rating(3), QuadrantOccupationRater.getRatingForTeam(gameState,Team.ONE));
        assertEquals(Rating.ONE, QuadrantOccupationRater.getRatingForTeam(gameState,Team.TWO));
    }

    @Test
    void rate() {
        Rater rater = new QuadrantOccupationRater();
        assertEquals(Rating.TWO, rater.rate(gameState));
    }
}