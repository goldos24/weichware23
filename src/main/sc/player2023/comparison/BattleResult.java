package sc.player2023.comparison;

public record BattleResult(int ownWins, int opponentWins) {
    public BattleResult plus(BattleResult other) {
        return new BattleResult(this.ownWins + other.ownWins, this.opponentWins + other.opponentWins);
    }

    public static final BattleResult EMPTY = new BattleResult(0, 0);
    public static final BattleResult ONE_WIN_FOR_OWN = new BattleResult(1, 0);
    public static final BattleResult ONE_WIN_FOR_OPPONENT = new BattleResult(0, 1);

    @Override
    public String toString() {
        return "BattleResult(" + ownWins + ", " + opponentWins + ")";
    }
}
