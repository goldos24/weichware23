package sc.player2023.comparison;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.*;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
public class Battle {
    @Nonnull
    public static BattleResult run(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter, @Nonnull BattleData battleData) {
        @Nonnull BattleResult result = BattleResult.EMPTY;
        for (int currentRun = 0; currentRun < battleData.runs(); ++currentRun) {
            result = result.plus(biDirectionalFight(ownFighter, opponentFighter, battleData));
        }
        return result;
    }

    @Nonnull
    private static BattleResult monoDirectionalFight(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter, @Nonnull ITeam ownFighterTeam, @Nonnull BattleData battleData) {
        @Nonnull ImmutableGameState gameState = battleData.testBoard() == null ? ImmutableGameStateFactory.createAny() : battleData.testBoard();
        ITeam opponentTeam = ownFighterTeam.opponent();
        Fighter fighter1 = ownFighterTeam == Team.ONE ? ownFighter : opponentFighter;
        Fighter fighter2 = ownFighterTeam == Team.TWO ? ownFighter : opponentFighter;
        while (gameState.stillRunning()) {
            Rater rater1 = fighter1.rater();
            MoveGetter moveGetter1 = fighter1.moveGetter();
            Move nextMove = moveGetter1.getBestMove(gameState, rater1);
            if(nextMove == null)
                break;
            gameState = GameRuleLogic.withMovePerformed(gameState, nextMove);
            if(gameState.isOver())
                break;
            nextMove = fighter2.moveGetter().getBestMove(gameState, fighter2.rater());
            if(nextMove == null)
                break;
            gameState = GameRuleLogic.withMovePerformed(gameState, nextMove);
        }
        if(GameRuleLogic.isTeamWinner(gameState, ownFighterTeam))
            return BattleResult.ONE_WIN_FOR_OWN;
        else if (GameRuleLogic.isTeamWinner(gameState, opponentTeam))
            return BattleResult.ONE_WIN_FOR_OPPONENT;
        return BattleResult.EMPTY;
    }

    @Nonnull
    private static BattleResult biDirectionalFight(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter, @Nonnull BattleData battleData) {
        return monoDirectionalFight(ownFighter, opponentFighter, Team.ONE, battleData).plus(monoDirectionalFight(ownFighter, opponentFighter, Team.TWO, battleData));
    }
}
