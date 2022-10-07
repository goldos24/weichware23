package sc.player2023.comparison;

public record BattleResult(int ownWins, int opponentWins) {
    public BattleResult plus(BattleResult other) {
        return new BattleResult(this.ownWins + other.ownWins, this.opponentWins + other.opponentWins);
    }

    public static BattleResult empty() {return new BattleResult(0, 0);}
}
