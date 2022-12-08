package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;

public record Statistics(long visits, long wins) {
    public static Statistics zeroed() {
        return new Statistics(0, 0);
    }

    public static Statistics ofPlayoutResult(PlayoutResult result, ITeam currentTeam) {
        return Statistics.zeroed().addPlayoutResult(result, currentTeam);
    }

    public Statistics addVisitAndWin() {
        return new Statistics(this.visits + 1, this.wins + 1);
    }

    public Statistics addVisit() {
        return new Statistics(this.visits + 1, this.wins);
    }

    public Statistics add(Statistics other) {
        return new Statistics(this.visits + other.visits, this.wins + other.wins);
    }

    /**
     * Returns a new Statistics instance, with wins = visits - wins
     * <br>
     * This is, in essence, the Statistics from the perspective of the opponent.
     *
     * @return Statistics with wins = visits - wins
     */
    public Statistics inverted() {
        return new Statistics(this.visits, this.visits - this.wins);
    }

    private Statistics addCurrentTeamBasedPlayoutResult(PlayoutResult result) {
        PlayoutResult.Kind kind = result.getKind();

        return switch (kind) {
            case WIN -> this.addVisitAndWin();  // Own win is good for the current team
            case LOSS, DRAW -> this.addVisit(); // Own loss/draw is bad for the current team
            case NONE -> this;
        };
    }

    private Statistics addOpponentTeamBasedPlayoutResult(PlayoutResult result) {
        PlayoutResult.Kind kind = result.getKind();

        return switch (kind) {
            case WIN, DRAW -> this.addVisit();  // Opponent win/draw is bad for the current team
            case LOSS -> this.addVisitAndWin(); // Opponent loss is good for the current team
            case NONE -> this;
        };
    }

    public Statistics addPlayoutResult(PlayoutResult result, ITeam currentTeam) {
        ITeam affectedTeam = result.getAffectedTeam();

        if (affectedTeam == currentTeam) {
            return this.addCurrentTeamBasedPlayoutResult(result);
        } else {
            return this.addOpponentTeamBasedPlayoutResult(result);
        }
    }
}
