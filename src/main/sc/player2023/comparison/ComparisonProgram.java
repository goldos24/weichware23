package sc.player2023.comparison;

import sc.player2023.logic.*;

public class ComparisonProgram {
    public static void main(String[] args) {
        Rater rater = new CompositeRater(new Rater[]{new WeightedRater(5, new StupidRater()), new PotentialFishRater()});
        Fighter mctsFighter = new Fighter(new PureMCTSMoveGetter(), rater);
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), rater);
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(1));
        System.out.println(result);
    }
}
