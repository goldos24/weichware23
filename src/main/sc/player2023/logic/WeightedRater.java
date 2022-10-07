package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;

public record WeightedRater(int weight, Rater originalRater) implements Rater {
    @Override
    public int rate(@NotNull ImmutableGameState gameState) {
        return originalRater().rate(gameState) * weight;
    }
}
