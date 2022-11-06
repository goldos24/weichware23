package sc.player2023.comparison;

import sc.player2023.logic.*;
import sc.player2023.logic.rating.*;

public class ComparisonProgram {
    public static void main(String[] args) {
        Rater rater = new CompositeRater(new Rater[]{new WeightedRater(5, new StupidRater()), new PotentialFishRater()});
        Rater enhancedRater = new CompositeRater(new Rater[] {rater, new WeightedRater(20, new UselessPenguinRater())});
        Rater hopefullyBetterRater = new CompositeRater(new Rater[] {rater, new WeightedRater(20, new PenguinCutOffRater())});
        Fighter mctsFighter = new Fighter(new PVSMoveGetter(), enhancedRater);
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), hopefullyBetterRater);
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(20));
        System.out.println(result);
    }
}
