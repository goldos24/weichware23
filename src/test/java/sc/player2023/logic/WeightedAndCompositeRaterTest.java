package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class WeightedAndCompositeRaterTest {

    private static final Rater CONSTANT_RATER = gameState -> 1;

    private static final ImmutableGameState testGameState = ImmutableGameStateFactory.createAny();

    @Test void weightedRaterTest() {
        Rater weightedRater = new WeightedRater(5, CONSTANT_RATER);
        assertEquals(5, weightedRater.rate(testGameState));
    }

    @Test void compositeRaterTest() {
        Rater compositeRater = new CompositeRater(new Rater[]{CONSTANT_RATER, CONSTANT_RATER});
        assertEquals(2, compositeRater.rate(testGameState));
    }

    @Test void mixedTest() {
        Rater constantRater = CONSTANT_RATER;
        Rater rater1 = new WeightedRater(2, constantRater);
        Rater rater2 = new WeightedRater(3, constantRater);
        Rater compositeRater = new CompositeRater(new Rater[]{rater1, rater2});
        assertEquals(5, compositeRater.rate(testGameState));
    }
}