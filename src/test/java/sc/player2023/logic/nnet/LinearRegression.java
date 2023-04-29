package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinearRegression {

    public static DataSet generateLinearRegressionModel(int dataPoints, List<Double> coefficients, double valueMin, double valueMax, double noiseStandardDeviation, Random random) {
        List<DataSetRow> rows = new ArrayList<>();
        for (int i = 0; i < dataPoints; ++i) {

            int inputsSize = coefficients.size() - 1;
            double[] inputs = new double[inputsSize];
            double y = coefficients.get(0);
            for (int j = 0; j < inputsSize; ++j) {
                double x = inputs[j] = random.nextDouble() * (valueMax - valueMin) + valueMin;
                y += coefficients.get(j + 1) * x;
            }

            double noise = random.nextGaussian() * noiseStandardDeviation;
            double[] outputs = {y + noise};
            rows.add(new DataSetRow(inputs, outputs));
        }

        return new DataSet(rows.toArray(new DataSetRow[0]));
    }
}
