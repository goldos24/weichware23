package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameOutcomePrediction;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.NeuralNetworkSerializer;
import sc.player2023.logic.nnet.data.GameOutcomePredictionConverter;
import sc.player2023.logic.nnet.data.GameStateConverter;
import sc.player2023.logic.nnet.data.NeuralNetworkConverter;
import sc.player2023.logic.nnet.data.OneHotBoardConverter;

import javax.annotation.Nonnull;
import java.io.IOException;

public class NeuralOutcomePredictionRater implements Rater {

    private final NeuralNetworkConverter<ImmutableGameState, GameOutcomePrediction> converter;

    public NeuralOutcomePredictionRater() {
        GameStateConverter gameStateConverter = new GameStateConverter(new OneHotBoardConverter());
        GameOutcomePredictionConverter predictionConverter = new GameOutcomePredictionConverter();

        NeuralNetwork network;
        try {
            network = NeuralNetworkSerializer.loadFromResource("outcome_predictor.nnet");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.converter = new NeuralNetworkConverter<>(gameStateConverter, predictionConverter, network);
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        GameOutcomePrediction prediction = converter.convert(gameState);

        ITeam currentTeam = gameState.getCurrentTeam();
        double winLikelihood = 0.0;
        if (currentTeam == Team.ONE) {
            winLikelihood = prediction.teamOneWinLikelihood();
        } else if (currentTeam == Team.TWO) {
            winLikelihood = prediction.teamTwoWinLikelihood();
        }

        return new Rating((int)(winLikelihood * 100));
    }
}
