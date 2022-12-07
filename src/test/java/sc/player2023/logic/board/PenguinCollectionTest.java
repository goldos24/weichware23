package sc.player2023.logic.board;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Penguin;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PenguinCollectionTest {

    @Test
    void fromOtherPenguinCollection() {
        List<Penguin> penguinCollection = List.of(new Penguin(new Coordinates(3, 1), Team.TWO));
        int expectedTeamTwo = 0b001001001;
        int got = PenguinCollection.fromOtherPenguinCollection(penguinCollection).getTeamTwoPenguins();
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
        List<Penguin> expected = List.of(new Penguin(new Coordinates(4, 2), Team.ONE),
                new Penguin(new Coordinates(3, 1), Team.ONE));
        List<Penguin> got = PenguinCollection.getPenguinStreamForTeam(bitset, Team.ONE).toList();
        assertEquals(expected, got);
    }

    @Test
    void iterator() {
        List<Penguin> expected = List.of(new Penguin(new Coordinates(4, 2), Team.ONE),
                new Penguin(new Coordinates(3, 1), Team.ONE),
                new Penguin(new Coordinates(3, 5), Team.TWO));
        PenguinCollection testCollection = PenguinCollection.fromOtherPenguinCollection(expected);
        List<Penguin> got = testCollection.stream().toList();
        assertEquals(expected, got);
    }

    @Test
    void withPenguinMoved() {
        Coordinates from = new Coordinates(3, 1);
        Coordinates to = new Coordinates(3, 3);
        Coordinates evilPenguin = new Coordinates(3, 5);
        Penguin ownInitialPenguin = new Penguin(from, Team.ONE);
        List<Penguin> initial = List.of(
                ownInitialPenguin,
                new Penguin(evilPenguin, Team.TWO));
        List<Penguin> expected = List.of(
                new Penguin(to, Team.ONE),
                new Penguin(evilPenguin, Team.TWO));
        PenguinCollection testCollection = PenguinCollection.fromOtherPenguinCollection(initial);
        testCollection = testCollection.withPenguinMoved(ownInitialPenguin, to);
        List<Penguin> got = testCollection.stream().toList();
        assertEquals(expected, got);
    }
}