package sc.player2023.logic;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class EmptyFieldCountBasedHybridMoveGetter implements MoveGetter {
    private final MoveGetter earlyGameMoveGetter;
    private final MoveGetter midGameMoveGetter;
    private final MoveGetter lateGameMoveGetter;
    private final int midGameStart;
    private final int lateGameStart;

    EmptyFieldCountBasedHybridMoveGetter(
            MoveGetter earlyGameMoveGetter,
            MoveGetter midGameMoveGetter,
            MoveGetter lateGameMoveGetter,
            int midGameStart,
            int lateGameStart
    ) {
        this.earlyGameMoveGetter = earlyGameMoveGetter;
        this.midGameMoveGetter = midGameMoveGetter;
        this.lateGameMoveGetter = lateGameMoveGetter;
        this.midGameStart = midGameStart;
        this.lateGameStart = lateGameStart;
    }

    public MoveGetter earlyGameMoveGetter() {
        return earlyGameMoveGetter;
    }

    public MoveGetter midGameMoveGetter() {
        return midGameMoveGetter;
    }

    public MoveGetter lateGameMoveGetter() {
        return lateGameMoveGetter;
    }

    public int midGameStart() {
        return midGameStart;
    }

    public int lateGameStart() {
        return lateGameStart;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmptyFieldCountBasedHybridMoveGetter) obj;
        return Objects.equals(this.earlyGameMoveGetter, that.earlyGameMoveGetter) &&
                Objects.equals(this.midGameMoveGetter, that.midGameMoveGetter) &&
                Objects.equals(this.lateGameMoveGetter, that.lateGameMoveGetter) &&
                this.midGameStart == that.midGameStart &&
                this.lateGameStart == that.lateGameStart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(earlyGameMoveGetter, midGameMoveGetter, lateGameMoveGetter, midGameStart, lateGameStart);
    }

    @Override
    public String toString() {
        return "EmptyFieldCountBasedHybridMoveGetter[" +
                "earlyGameMoveGetter=" + earlyGameMoveGetter + ", " +
                "midGameMoveGetter=" + midGameMoveGetter + ", " +
                "lateGameMoveGetter=" + lateGameMoveGetter + ", " +
                "midGameStart=" + midGameStart + ", " +
                "lateGameStart=" + lateGameStart + ']';
    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        int emptyFieldCount = GameRuleLogic.getEmptyFieldCountFromBoard(gameState.getBoard());
        if (emptyFieldCount < midGameStart) {
            return earlyGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
        }
        if (emptyFieldCount < lateGameStart) {
            return midGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
        }
        return lateGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
    }
}
