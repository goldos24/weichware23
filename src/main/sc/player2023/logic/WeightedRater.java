package sc.player2023.logic;

import javax.annotation.Nonnull;

public record WeightedRater(int weight, Rater originalRater) implements Rater {
    
    @Override
    public int rate(@Nonnull ImmutableGameState gameState) {
        return originalRater.rate(gameState) * weight;
    }
    
}
