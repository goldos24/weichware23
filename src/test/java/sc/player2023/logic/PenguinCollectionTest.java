package sc.player2023.logic;

import kotlin.Pair;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PenguinCollectionTest {

    @Test
    void fromOtherPenguinCollection() {
        List<Pair<Coordinates, Team>> penguinCollection = List.of(new Pair<>(new Coordinates(3, 1), Team.TWO));
        int expectedTeamTwo = 0b00100101;
        int got = PenguinCollection.fromOtherPenguinCollection(penguinCollection).teamTwoPenguins;
        assertEquals(expectedTeamTwo, got);
    }

    @Test void getPenguinCountInBitSet() {
        int testBitSet = 0b00100101;
        assertEquals(1, PenguinCollection.getPenguinCountInBitSet(testBitSet));
    }

    @Test void getCoordsAtBitSetIndex() {
        int testBitSet = 0b00100101;
        Coordinates expected = new Coordinates(3, 1);
        Coordinates got = PenguinCollection.getCoordsAtBitSetIndex(testBitSet, 0);
        assertEquals(expected, got);
    }
}