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
}