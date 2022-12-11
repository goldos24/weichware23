package sc.player2023.comparison;

import sc.player2023.logic.*;
import sc.player2023.logic.pvs.ShuffledPossibleMovePVSMoveGetter;
import sc.player2023.logic.pvs.TransPositionTablePVSMoveGetter;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter conventionalFighter = new Fighter(new ShuffledPossibleMovePVSMoveGetter(), Logic.createCombinedRater());
        Fighter transPositionTableFighter = new Fighter(new TransPositionTablePVSMoveGetter(),Logic.createCombinedRater());
        BattleResult result = Battle.run(conventionalFighter, transPositionTableFighter, new BattleData(5));
        System.out.println(result);
    }
}
