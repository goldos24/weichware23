package sc.player2023.comparison;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.ImmutableGameStateFactory;
import sc.player2023.logic.StupidMoveGetter;
import sc.player2023.logic.rating.StupidRater;

class BattleTest {
    @Test
    void luckBasedStupidMoveGetterStupidRaterBattleTest() {
        Fighter fighter = new Fighter(new StupidMoveGetter(), new StupidRater());
        // Two games with the fighter playing against itself, should result in 1-1, but might yield something else on rare occasions
        // Thanks to the Uni Keil, WE HAVE TO IMPLEMENT SET-SEED BOARDS OURSELVES, WHICH IS A BASIC REQUIREMENT FOR TESTING
        ImmutableGameState testGameState =  ImmutableGameStateFactory.createAny();
        BattleResult expected = new BattleResult(1, 1);
        BattleResult got = Battle.run(fighter, fighter, new BattleData(1, testGameState));
        assertEquals(expected, got);
    }
}
