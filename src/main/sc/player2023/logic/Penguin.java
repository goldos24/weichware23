package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author Till Fransson
 * @since 17.11.2022
 */
public final class Penguin {
    private final Coordinates coordinates;
    private final Team team;

    public Penguin(Coordinates coordinates, Team team) {
        this.coordinates = coordinates;
        this.team = team;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    public Team team() {
        return team;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Penguin) obj;
        return Objects.equals(this.coordinates, that.coordinates) &&
                Objects.equals(this.team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, team);
    }

    @Override
    public String toString() {
        return "Penguin[" +
                "coordinates=" + coordinates + ", " +
                "team=" + team + ']';
    }
}
