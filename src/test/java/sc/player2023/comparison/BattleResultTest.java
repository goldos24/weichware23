package sc.player2023.comparison;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleResultTest {
    @Test
    void equalsTest() {
        int ownWins = 2, otherWins = 3;
        BattleResult result1 = new BattleResult(ownWins, otherWins);
        BattleResult result2 = new BattleResult(ownWins, otherWins);
        assertEquals(result1, result2);
    }

    @Test
    void plusTest() {
        int ownWinsSummand1 = 2, ownWinsSummand2 = 3, opponentWinsSummand1 = 4, opponentWinsSummand2 = 5;
        int expectedOwnWinsResult = ownWinsSummand1 + ownWinsSummand2, expectedOpponentWinsResult = opponentWinsSummand1 + opponentWinsSummand2;
        BattleResult summand1 = new BattleResult(ownWinsSummand1, opponentWinsSummand1);
        BattleResult summand2 = new BattleResult(ownWinsSummand2, opponentWinsSummand2);
        BattleResult result1 = summand1.plus(summand2);
        BattleResult result2 = summand2.plus(summand1);
        BattleResult expected = new BattleResult(expectedOwnWinsResult, expectedOpponentWinsResult);
        assertEquals(result1, result2);
        assertEquals(result1, expected);
        assertEquals(result2, expected);
    }
}