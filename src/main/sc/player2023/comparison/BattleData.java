package sc.player2023.comparison;

import sc.player2023.logic.ImmutableGameState;

import javax.annotation.Nullable;
import java.util.Objects;

public record BattleData(int runs, @Nullable ImmutableGameState testBoard) {
    BattleData(int runs) {
        this(runs, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BattleData that = (BattleData) o;
        return runs == that.runs && Objects.equals(testBoard, that.testBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runs, testBoard);
    }

    @Override
    public String toString() {
        return "BattleData {" + "runs = " + runs + ", " + testBoard + "}";
    }
}
