package sc.player2023.logic;

import kotlin.Pair;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    void getBitSetWithPenguinAtPos() {
        int initial = 0b01001010;
        int expected = 0b00100101001010;
        int got = PenguinCollection.getBitSetWithPenguinAtPos(new Coordinates(3, 1), initial, 1);
        assertEquals(expected, got);
    }

    @Test
    void getPenguinStreamForTeam() {
        int bitset = 0b00100101001010;
        List<Pair<Coordinates, Team>> expected = List.of(new Pair<>(new Coordinates(4, 2), Team.ONE),
                new Pair<>(new Coordinates(3, 1), Team.ONE));
        List<Pair<Coordinates, Team>> got = PenguinCollection.getPenguinStreamForTeam(bitset, Team.ONE).toList();
        assertEquals(expected, got);
    }

    @Test
    void iterator() {
        List<Pair<Coordinates, Team>> expected = List.of(new Pair<>(new Coordinates(4, 2), Team.ONE),
                new Pair<>(new Coordinates(3, 1), Team.ONE),
                new Pair<>(new Coordinates(3, 5), Team.TWO));
        List<Pair<Coordinates, Team>> got = new ArrayList<>();
        PenguinCollection testCollection = PenguinCollection.fromOtherPenguinCollection(expected);
        for (var penguin : testCollection) {
            got.add(penguin);
        }
        assertEquals(expected, got);
    }
}