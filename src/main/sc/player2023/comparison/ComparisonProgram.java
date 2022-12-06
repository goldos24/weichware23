package sc.player2023.comparison;

import sc.player2023.logic.*;
import sc.player2023.logic.pvs.PVSMoveGetter;
import sc.player2023.logic.rating.*;

public class ComparisonProgram {

    public static Rater createCombinedRater() {
        CappedFishDifferenceRater cappedFishDifferenceRater = new CappedFishDifferenceRater();
        WeightedRater weightedStupidRater = new WeightedRater(5, cappedFishDifferenceRater);
        Rater weightedUselessPenguinRater = new WeightedRater(20, new PenguinCutOffRater());
        Rater[] raters = {weightedStupidRater, new WeightedRater(3, new PotentialFishRater()),
                weightedUselessPenguinRater, new WeightedRater(5, new ReachableFishRater()) };
        return new CompositeRater(raters);
    }


    public static void main(String[] args) {
        Fighter mctsFighter = new Fighter(new PVSMoveGetter(), Logic.createCombinedRater());
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), createCombinedRater());
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(5));
        System.out.println(result);
    }
}
