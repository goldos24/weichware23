package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.pvs.NegaMaxMoveGetter;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter newRaterFighter = new Fighter(new AspiredPVSMoveGetter(), Logic.createNewCombinedRater());
        Fighter oldRaterFighter = new Fighter(new AspiredPVSMoveGetter(), Logic.createCombinedRater());
        BattleResult result = Battle.run(oldRaterFighter, newRaterFighter, new BattleData(5));
        System.out.println(result);
    }
}
