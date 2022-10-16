package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sc.player2023.logic.Rating.FIVE;
import static sc.player2023.logic.Rating.TWO;


class WeightedAndCompositeRaterTest {

    private static final Rater CONSTANT_RATER = gameState -> Rating.ONE;

    private static final ImmutableGameState testGameState = ImmutableGameStateFactory.createAny();

    @Test
    void weightedRaterTest() {
        Rater weightedRater = new WeightedRater(5, CONSTANT_RATER);
        assertEquals(FIVE, weightedRater.rate(testGameState));
    }

    @Test
    void compositeRaterTest() {
        Rater compositeRater = new CompositeRater(new Rater[]{CONSTANT_RATER, CONSTANT_RATER});
        assertEquals(TWO, compositeRater.rate(testGameState));
    }

    @Test
    void mixedTest() {
        Rater constantRater = CONSTANT_RATER;
        Rater rater1 = new WeightedRater(2, constantRater);
        Rater rater2 = new WeightedRater(3, constantRater);
        Rater compositeRater = new CompositeRater(new Rater[]{rater1, rater2});
        assertEquals(FIVE, compositeRater.rate(testGameState));
    }
}
