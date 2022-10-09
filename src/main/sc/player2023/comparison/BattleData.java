package sc.player2023.comparison;

import sc.player2023.logic.ImmutableGameState;

import javax.annotation.Nullable;

public record BattleData(int runs, @Nullable ImmutableGameState testBoard) {
    BattleData(int runs) {
        this(runs, null);
    }
}
