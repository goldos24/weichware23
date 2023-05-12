package sc.player2023.logic.nnet.dojos;

import sc.player2023.logic.GameOutcomePrediction;
import sc.player2023.logic.GameOutcomePredictor;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.gameState.ImmutableGameStateFactory;
import sc.player2023.logic.nnet.data.GameStateConverter;
import sc.player2023.logic.nnet.data.OneHotBoardConverter;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OutcomePredictionData {

    private final Random random = new Random();

    private final int simulatedGames;
    private final int dataPoints;

    private final GameStateConverter gameStateConverter;

    public OutcomePredictionData(int simulatedGames, int dataPoints) {
        this.simulatedGames = simulatedGames;
        this.dataPoints = dataPoints;
        this.gameStateConverter = new GameStateConverter(new OneHotBoardConverter());
    }

    public static ImmutableGameState createAnyNotOverGameState(Random random) {
        ImmutableGameState gameState = ImmutableGameStateFactory.createAny();
        for (int i = 0; i < random.nextInt(45); ++i) {
            ImmutableGameState nextState = GameRuleLogic.withRandomMovePerformed(gameState);
            if (nextState.isOver())
                break;

            gameState = nextState;
        }

        return gameState;
    }

    private DataSetRow createRow() {
        ImmutableGameState gameState = createAnyNotOverGameState(this.random);
        double[] inputs = this.gameStateConverter.convert(gameState);
        GameOutcomePrediction prediction = GameOutcomePredictor.doPredictionForGameState(gameState, this.simulatedGames);
        double[] outputs = new double[] {
            prediction.teamOneWinLikelihood(),
            prediction.teamTwoWinLikelihood(),
            prediction.teamOneAveragePointRatio(),
            prediction.teamTwoAveragePointRatio()
        };
        return new DataSetRow(inputs, outputs);
    }

    public DataSet createDataSet() {
        ConsoleOutputUtil.disableIO();
        ConsoleProgressUpdate progress = new ConsoleProgressUpdate(ConsoleOutputUtil.getOut(), "Creating training data", this.dataPoints);
        List<DataSetRow> rows = new ArrayList<>();
        for (int i = 0; i < this.dataPoints; ++i) {
            rows.add(this.createRow());
            progress.printStateAndIncrement();
        }
        ConsoleOutputUtil.enableIO();

        DataSetRow[] rowsArray = rows.toArray(new DataSetRow[0]);
        return new DataSet(rowsArray);
    }
}
