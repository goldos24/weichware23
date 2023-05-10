package sc.player2023.logic.nnet.data;

import sc.player2023.logic.GameOutcomePrediction;

public class GameOutcomePredictionConverter implements NetworkOutputConverter<GameOutcomePrediction> {
    @Override
    public GameOutcomePrediction convert(double[] outputs) {
        return new GameOutcomePrediction(
            outputs[0], outputs[1],
            outputs[2], outputs[3]
        );
    }

    @Override
    public int size() {
        return 4;
    }
}
