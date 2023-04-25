package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.move.FishCountPreSorting;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter newRaterFighter = new Fighter(new AspiredPVSMoveGetter(), Logic.createNewCombinedRater());
        Fighter oldRaterFighter = new Fighter(new AspiredPVSMoveGetter(FishCountPreSorting::getPossibleMoves),
                Logic.createNewCombinedRater());
        BattleResult result = Battle.run(oldRaterFighter, newRaterFighter, new BattleData(5));
        System.out.println(result);
    }
}
