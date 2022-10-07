package sc.player2023.comparison;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.ImmutableGameStateFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
public class Battle {
    @Nonnull
    public static BattleResult run(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter, @Nonnull BattleData battleData) {
        @Nonnull BattleResult result = BattleResult.EMPTY;
        for (int currentRun = 0; currentRun < battleData.runs(); ++currentRun) {
            result = result.plus(biDirectionalFight(ownFighter, opponentFighter));
        }
        return result;
    }

    @Nonnull
    private static BattleResult monoDirectionalFight(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter, @Nonnull ITeam ownFighterTeam) {
        ImmutableGameState gameState = ImmutableGameStateFactory.createAny();
        ITeam opponentTeam = ownFighterTeam.opponent();
        Fighter fighter1 = ownFighterTeam == Team.ONE ? ownFighter : opponentFighter;
        Fighter fighter2 = ownFighterTeam == Team.TWO ? ownFighter : opponentFighter;
        while (gameState.stillRunning()) {
            Move nextMove = fighter1.moveGetter().getBestMove(gameState, Team.ONE, fighter1.rater());
            if(nextMove == null)
                break;
            gameState = gameState.withMove(nextMove);
            if(gameState.isOver())
                break;
            nextMove = fighter2.moveGetter().getBestMove(gameState, Team.TWO, fighter2.rater());
            if(nextMove == null)
                break;
            gameState = gameState.withMove(nextMove);
        }
        if(GameRuleLogic.isTeamWinner(gameState, ownFighterTeam))
            return BattleResult.ONE_WIN_FOR_OWN;
        else if (GameRuleLogic.isTeamWinner(gameState, opponentTeam))
            return BattleResult.ONE_WIN_FOR_OPPONENT;
        return BattleResult.EMPTY;
    }

    @Nonnull
    private static BattleResult biDirectionalFight(@Nonnull Fighter ownFighter, @Nonnull Fighter opponentFighter) {
        return monoDirectionalFight(ownFighter, opponentFighter, Team.ONE).plus(monoDirectionalFight(ownFighter, opponentFighter, Team.TWO));
    }
}
