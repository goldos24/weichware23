package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter conventionalFighter = new Fighter(new FailSoftPVSMoveGetter(), Logic.createCombinedRater());
        Fighter aspiredFighter = new Fighter(new AspiredPVSMoveGetter(), Logic.createCombinedRater());
        BattleResult result = Battle.run(conventionalFighter, aspiredFighter, new BattleData(5));
        System.out.println(result);
    }
}
