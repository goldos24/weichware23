package sc.player2023.comparison;

import sc.player2023.logic.*;
import sc.player2023.logic.enemy.MaxRating;
import sc.player2023.logic.enemy.MoveFinder;

public class ComparisonProgram {
    public static void main(String[] args) {
        Fighter mctsFighter = new Fighter(new PVSMoveGetter(), Logic.createCombinedRater());
        Fighter pvsFighter = new Fighter(new MoveFinder(), new MaxRating());
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(5));
        System.out.println(result);
    }
}
