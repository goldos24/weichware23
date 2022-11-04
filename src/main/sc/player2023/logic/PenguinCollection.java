package sc.player2023.logic;

import kotlin.NotImplementedError;
import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PenguinCollection implements Collection<Pair<Coordinates, Team>> {
    int teamOnePenguins, teamTwoPenguins;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return teamOnePenguins == 0 && teamTwoPenguins == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }
    private static final int penguinCountBitMask = 0b111;
    private static final int penguinInitialOffset = 3;
    private static final int penguinCoordsOffset = 6;
    private static final int penguinCoordOffset = 3;
    private static final int penguinCoordsBitMask = 0b111111;
    private static final int penguinCoordBitMask = 0b111;

    PenguinCollection(int teamOnePenguins, int teamTwoPenguins) {
        this.teamOnePenguins = teamOnePenguins;
        this.teamTwoPenguins = teamTwoPenguins;
    }

    public static PenguinCollection fromOtherPenguinCollection(Collection<Pair<Coordinates, Team>> other) {
        int teamOne = 0, teamTwo = 0;
        for(var penguin : other) {
            int current = penguin.getSecond() == Team.ONE ? teamOne : teamTwo;
            current = bitsetWithPenguinAdded(penguin.getFirst(), current);
            if(penguin.getSecond() == Team.ONE) {
                teamOne = current;
            }
            else {
                teamTwo = current;
            }
        }
        return new PenguinCollection(teamOne, teamTwo);
    }

    static int bitsetWithPenguinAdded(Coordinates penguinCoords, int bitset) {
        int teamPenguinCount = bitset & penguinCountBitMask;
        bitset = (bitset & ~penguinCountBitMask) | ((teamPenguinCount +1)& penguinCountBitMask);
        bitset = getBitSetWithPenguinAtPos(penguinCoords, bitset, teamPenguinCount);
        return bitset;
    }

    public PenguinCollection withExtraPenguin(Pair<Coordinates, Team> penguin) {
        Team team = penguin.getSecond();
        return new PenguinCollection(
                team == Team.ONE ? bitsetWithPenguinAdded(penguin.getFirst(), teamOnePenguins) : teamOnePenguins,
                team == Team.TWO ? bitsetWithPenguinAdded(penguin.getFirst(), teamTwoPenguins) : teamTwoPenguins
        );
    }


    static int getPenguinCountInBitSet(int bitset) {
        return bitset & penguinCountBitMask;
    }

    static Coordinates getCoordsAtBitSetIndex(int bitset, int index) {
        int startPos = penguinInitialOffset + index * penguinCoordsOffset;
        int yStartPos = startPos + penguinCoordOffset;
        int x = (bitset & (penguinCoordBitMask << startPos)) >> startPos;
        int y = (bitset & (penguinCoordBitMask << yStartPos)) >> yStartPos;
        return new Coordinates(x*2+y%2, y);
    }

    static int getBitSetWithPenguinAtPos(Coordinates penguinCoords, int current, int position) {
        int x = penguinCoords.getX()/2; // compensate for Uni Keil alternating X
        int y = penguinCoords.getY();
        int combinedCoords = ((y << penguinCoordOffset) | x) & penguinCoordsBitMask;
        int positionOffset = position * penguinCoordsOffset +penguinInitialOffset;
        int nonCoordBitMask = ~(penguinCoordsBitMask << positionOffset);
        current = (current & nonCoordBitMask) | (combinedCoords << positionOffset);
        return current;
    }

    static Stream<Pair<Coordinates, Team>> getPenguinStreamForTeam(int bitset, Team team) {
        int length = bitset & penguinCountBitMask;
        IntStream indices = IntStream.iterate(0, index -> index < length, index -> index + 1);
        return indices.mapToObj(index -> new Pair<>(getCoordsAtBitSetIndex(bitset, index), team));
    }

    @Nonnull
    @Override
    public Iterator<Pair<Coordinates, Team>> iterator() {
        return stream().iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        throw new NotImplementedError();
    }

    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        throw new NotImplementedError();
    }

    @Override
    public boolean add(Pair<Coordinates, Team> coordinatesTeamPair) {
        throw new NotImplementedError();
    }

    @Override
    public boolean remove(Object o) {
        throw new NotImplementedError();
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        throw new NotImplementedError();
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends Pair<Coordinates, Team>> c) {
        throw new NotImplementedError();
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        throw new NotImplementedError();
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        throw new NotImplementedError();
    }

    @Override
    public void clear() {

    }

    @Override
    public Stream<Pair<Coordinates, Team>> stream() {
        var streamTeamOne = getPenguinStreamForTeam(teamOnePenguins, Team.ONE);
        var streamTeamTwo = getPenguinStreamForTeam(teamTwoPenguins, Team.TWO);
        return Stream.concat(streamTeamOne, streamTeamTwo);
    }
}
