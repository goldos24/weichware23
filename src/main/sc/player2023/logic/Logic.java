package sc.player2023.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.api.plugins.IGameState;
import sc.player.IGameHandler;
import sc.player2023.logic.rating.*;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;
import sc.shared.GameResult;

import javax.annotation.Nonnull;

/**
 * Das Herz des Clients:
 * Eine sehr simple Logik, die ihre Zuege zufaellig waehlt,
 * aber gueltige Zuege macht.
 * <p>
 * Ausserdem werden zum Spielverlauf Konsolenausgaben gemacht.
 */
public class Logic implements IGameHandler {
  private static final Logger log = LoggerFactory.getLogger(Logic.class);

  /** Aktueller Spielstatus. */
  private GameState gameState;

  public static final int MAX_TIME = 1900;

  public static TimeMeasurer createDefaultRunningTimeMeasurer() {
    TimeMeasurer result = new TimeMeasurer(MAX_TIME);
    result.reset();
    return result;
  }
  public static Rater createCombinedRater() {
    StupidRater stupidRater = new StupidRater();
    WeightedRater weightedStupidRater = new WeightedRater(5, stupidRater);
    Rater weightedUselessPenguinRater = new WeightedRater(20, new PenguinCutOffRater());
    Rater[] raters = {weightedStupidRater, new PotentialFishRater(), weightedUselessPenguinRater, new EdgePenguinPenalty()};
    return new CompositeRater(raters);
  }

  public void onGameOver(@Nonnull GameResult data) {
    log.info("Das Spiel ist beendet, Ergebnis: {}", data);
  }

  @Override
  @Nonnull
  public Move calculateMove() {
    long startTime = System.currentTimeMillis();
    log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());
    log.info("Anzahl möglicher Züge: {}", gameState.getPossibleMoves().size());

    MoveGetter moveGetter = new PVSMoveGetter();

    ImmutableGameState immutableGameState = ImmutableGameStateFactory.createFromGameState(gameState);
    TimeMeasurer timeMeasurer = createDefaultRunningTimeMeasurer();
    Move move = moveGetter.getBestMove(immutableGameState, createCombinedRater(), timeMeasurer);
    log.info("Sende {} nach {}ms.", move, System.currentTimeMillis() - startTime);
    return move;
  }

  @Override
  public void onUpdate(@Nonnull IGameState gameState) {
    this.gameState = (GameState) gameState;
    log.info("Zug: {} Dran: {}", gameState.getTurn(), gameState.getCurrentTeam());
  }

  @Override
  public void onError(@Nonnull String error) {
    log.warn("Fehler: {}", error);
  }
}
