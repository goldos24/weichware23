package sc.player2023.logic.nnet.data;

import sc.api.plugins.Team;
import sc.player2023.logic.gameState.ImmutableGameState;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class GameStateConverter implements NetworkInputConverter<ImmutableGameState> {

    private final BoardConverter boardConverter;

    public GameStateConverter() {
        this.boardConverter = new BoardConverter();
    }

    @Override
    public double[] convert(ImmutableGameState gameState) {
        double[] boardData = this.boardConverter.convert(gameState.getBoard());

        int teamOneScore = gameState.getScoreForTeam(Team.ONE).score();
        int teamTwoScore = gameState.getScoreForTeam(Team.TWO).score();
        int scoreSum = teamOneScore + teamTwoScore;
        double[] scoreData = new double[] {
            teamOneScore / (double) scoreSum,
            teamTwoScore / (double) scoreSum
        };

        DoubleStream boardAndScoreStream = DoubleStream.concat(Arrays.stream(boardData), Arrays.stream(scoreData));
        return boardAndScoreStream.toArray();
    }
}
