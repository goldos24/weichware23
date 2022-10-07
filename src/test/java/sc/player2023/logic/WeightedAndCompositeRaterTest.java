package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class WeightedAndCompositeRaterTest {
    private static class HighlyIntelligentTestRater implements Rater {

        @Override
        public int rate(@NotNull ImmutableGameState gameState) {
            return 1;
        }
    }

    private static final ImmutableGameState testGameState = ImmutableGameStateFactory.createAny();

    @Test void weightedRaterTest() {
        Rater weightedRater = new WeightedRater(5, new HighlyIntelligentTestRater());
        assertEquals(5, weightedRater.rate(testGameState));
    }

    @Test void compositeRaterTest() {
        Rater compositeRater = new CompositeRater(new HighlyIntelligentTestRater[]{new HighlyIntelligentTestRater(), new HighlyIntelligentTestRater()});
        assertEquals(2, compositeRater.rate(testGameState));
    }

    @Test void mixedTest() {
        Rater highlyIntelligentRater = new HighlyIntelligentTestRater();
        Rater rater1 = new WeightedRater(2, highlyIntelligentRater);
        Rater rater2 = new WeightedRater(3, highlyIntelligentRater);
        Rater compositeRater = new CompositeRater(new Rater[]{rater1, rater2});
        assertEquals(5, compositeRater.rate(testGameState));
    }
}