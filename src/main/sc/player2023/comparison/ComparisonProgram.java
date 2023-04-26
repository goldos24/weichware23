package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.move.FishCountPreSorting;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter oldRaterFighter = new Fighter(new AspiredPVSMoveGetter(), Logic.createNewCombinedRater());
        Fighter newRaterFighter = new Fighter(new AspiredPVSMoveGetter(FishCountPreSorting::getPossibleMoves),
                Logic.createNewCombinedRater());
        BattleResult result = Battle.run(newRaterFighter, oldRaterFighter, new BattleData(5));
        System.out.println(result);
    }
}
