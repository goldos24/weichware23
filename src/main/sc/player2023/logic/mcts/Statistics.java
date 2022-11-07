package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;

public record Statistics(int visits, int wins) {
    public static Statistics zeroed() {
        return new Statistics(0, 0);
    }

    public static Statistics ofPlayoutResult(PlayoutResult result, ITeam currentTeam) {
        return Statistics.zeroed().addPlayoutResult(result, currentTeam);
    }

    public Statistics addWin() {
        return new Statistics(this.visits + 1, this.wins + 1);
    }

    public Statistics addLossOrDraw() {
        return new Statistics(this.visits + 1, this.wins);
    }

    public Statistics add(Statistics other) {
        return new Statistics(this.visits + other.visits, this.wins + other.wins);
    }

    private Statistics addCurrentTeamBasedPlayoutResult(PlayoutResult result) {
        PlayoutResult.Kind kind = result.getKind();

        return switch (kind) {
            case WIN -> this.addWin();
            case LOSS, DRAW -> this.addLossOrDraw();
            case NONE -> this;
        };
    }

    private Statistics addOpponentTeamBasedPlayoutResult(PlayoutResult result) {
        PlayoutResult.Kind kind = result.getKind();

        return switch (kind) {
            case WIN -> this.addLossOrDraw();
            case LOSS, DRAW -> this.addWin();
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
