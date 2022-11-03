package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

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
    private static final int fishCountBitMask = 0b11;
    private static final int penguinInitialOffset = 2;
    private static final int penguinCoordOffset = 6;
    private static final int penguinCoordBitMask = 0b111111;

    private PenguinCollection(int teamOnePenguins, int teamTwoPenguins) {
        this.teamOnePenguins = teamOnePenguins;
        this.teamTwoPenguins = teamTwoPenguins;
    }

    public static PenguinCollection fromOtherPenguinCollection(Collection<Pair<Coordinates, Team>> other) {
        int teamOne = 0, teamTwo = 0;
        for(var penguin : other) {
            int current = penguin.getSecond() == Team.ONE ? teamOne : teamTwo;
            int teamPenguinCount = current & fishCountBitMask;
            current = (current & ~fishCountBitMask) | ((teamPenguinCount+1)&fishCountBitMask);
            int x = penguin.getFirst().getX()/2; // compensate for Uni Keil alternating X
            int y = penguin.getFirst().getY();
            int combinedCoords = ((y << penguinCoordOffset/2) | x) & penguinCoordBitMask;
            int positionOffset = (teamPenguinCount)*penguinCoordOffset+penguinInitialOffset;
            int nonCoordBitMask = ~(penguinCoordBitMask << positionOffset);
            current = (current & nonCoordBitMask) | (combinedCoords << positionOffset);
            if(penguin.getSecond() == Team.ONE) {
                teamOne = current;
            }
            else {
                teamTwo = current;
            }
        }
        return new PenguinCollection(teamOne, teamTwo);
    }

    @Nonnull
    @Override
    public Iterator<Pair<Coordinates, Team>> iterator() {
        return null;
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        return null;
    }

    @Override
    public boolean add(Pair<Coordinates, Team> coordinatesTeamPair) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends Pair<Coordinates, Team>> c) {
        return false;
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
