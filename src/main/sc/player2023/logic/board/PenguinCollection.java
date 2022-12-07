package sc.player2023.logic.board;

import kotlin.NotImplementedError;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Penguin;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PenguinCollection implements Collection<Penguin> {
    private final int teamOnePenguins, teamTwoPenguins;

    int getTeamOnePenguins() {
        return teamOnePenguins;
    }

    int getTeamTwoPenguins() {
        return teamTwoPenguins;
    }

    @Override
    public int size() {
        return getPenguinCountInBitSet(teamOnePenguins) + getPenguinCountInBitSet(teamTwoPenguins);
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

    public static PenguinCollection fromOtherPenguinCollection(Collection<Penguin> other) {
        int teamOne = 0, teamTwo = 0;
        for(var penguin : other) {
            int current = penguin.team() == Team.ONE ? teamOne : teamTwo;
            current = bitsetWithPenguinAdded(penguin.coordinates(), current);
            if(penguin.team() == Team.ONE) {
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

    public PenguinCollection withExtraPenguin(Penguin penguin) {
        Team team = penguin.team();
        return new PenguinCollection(
                team == Team.ONE ? bitsetWithPenguinAdded(penguin.coordinates(), teamOnePenguins) : teamOnePenguins,
                team == Team.TWO ? bitsetWithPenguinAdded(penguin.coordinates(), teamTwoPenguins) : teamTwoPenguins
        );
    }

    static int bitsetWithPenguinMoved(Coordinates from, Coordinates to, int bitset) {
        int penguinCount = getPenguinCountInBitSet(bitset);
        int i;
        for(i = 0; i < penguinCount+1; ++i) {
            if(getCoordsAtBitSetIndex(bitset, i).equals(from)) {
                break;
            }
        }
        if(i==penguinCount)
            throw new IllegalStateException("404: penguin not found");
        return getBitSetWithPenguinAtPos(to, bitset, i);
    }

    public PenguinCollection withPenguinMoved(Penguin penguin, Coordinates target) {
        var team = penguin.team();
        var from = penguin.coordinates();
        return new PenguinCollection(
                team == Team.ONE ? bitsetWithPenguinMoved(from, target, teamOnePenguins) : teamOnePenguins,
                team == Team.TWO ? bitsetWithPenguinMoved(from, target, teamTwoPenguins) : teamTwoPenguins
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

    static Stream<Penguin> getPenguinStreamForTeam(int bitset, Team team) {
        int length = bitset & penguinCountBitMask;
        IntStream indices = IntStream.iterate(0, index -> index < length, index -> index + 1);
        return indices.mapToObj(index -> new Penguin(getCoordsAtBitSetIndex(bitset, index), team));
    }

    public Stream<Penguin> streamForTeam(Team team) {
        return getPenguinStreamForTeam(team == Team.ONE? teamOnePenguins : teamTwoPenguins, team);
    }

    @Nonnull
    @Override
    public Iterator<Penguin> iterator() {
        return stream().iterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PenguinCollection{ ");
        for(Penguin penguin : this) {
            stringBuilder.append(penguin);
        }
        return stringBuilder.append(" }").toString();
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
    public boolean add(Penguin penguin) {
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
    public boolean addAll(@Nonnull Collection<? extends Penguin> c) {
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
        throw new NotImplementedError();
    }

    @Override
    public Stream<Penguin> stream() {
        var streamTeamOne = getPenguinStreamForTeam(teamOnePenguins, Team.ONE);
        var streamTeamTwo = getPenguinStreamForTeam(teamTwoPenguins, Team.TWO);
        return Stream.concat(streamTeamOne, streamTeamTwo);
    }
}
