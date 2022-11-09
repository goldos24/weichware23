package sc.player2023.logic.board;

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
        int expectedTeamTwo = 0b001001001;
        int got = PenguinCollection.fromOtherPenguinCollection(penguinCollection).teamTwoPenguins;
        assertEquals(expectedTeamTwo, got);
    }

    @Test void getPenguinCountInBitSet() {
        int testBitSet = 0b001001001;
        assertEquals(1, PenguinCollection.getPenguinCountInBitSet(testBitSet));
    }

    @Test void getCoordsAtBitSetIndex() {
        int testBitSet = 0b001001001;
        Coordinates expected = new Coordinates(3, 1);
        Coordinates got = PenguinCollection.getCoordsAtBitSetIndex(testBitSet, 0);
        assertEquals(expected, got);
    }

    @Test
    void getBitSetWithPenguinAtPos() {
        int initial = 0b010010010;
        int expected = 0b001001010010010;
        int got = PenguinCollection.getBitSetWithPenguinAtPos(new Coordinates(3, 1), initial, 1);
        assertEquals(expected, got);
    }

    @Test
    void bitsetWithPenguinAdded() {
        int initial = 0b010010001;
        int expected = 0b001001010010010;
        int got = PenguinCollection.bitsetWithPenguinAdded(new Coordinates(3, 1), initial);
        assertEquals(expected, got);
    }

    @Test
    void getPenguinStreamForTeam() {
        int bitset = 0b001001010010010;
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

    @Test
    void withPenguinMoved() {
        Coordinates from = new Coordinates(3, 1);
        Coordinates to = new Coordinates(3, 3);
        Coordinates evilPenguin = new Coordinates(3, 5);
        Pair<Coordinates, Team> ownInitialPenguin = new Pair<>(from, Team.ONE);
        List<Pair<Coordinates, Team>> initial = List.of(
                ownInitialPenguin,
                new Pair<>(evilPenguin, Team.TWO));
        List<Pair<Coordinates, Team>> expected = List.of(
                new Pair<>(to, Team.ONE),
                new Pair<>(evilPenguin, Team.TWO));
        PenguinCollection testCollection = PenguinCollection.fromOtherPenguinCollection(initial);
        testCollection = testCollection.withPenguinMoved(ownInitialPenguin, to);
        List<Pair<Coordinates, Team>> got = testCollection.stream().toList();
        assertEquals(expected, got);
    }
}