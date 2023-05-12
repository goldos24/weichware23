package sc.player2023.logic.nnet.data;

import sc.api.plugins.Team;
import sc.player2023.logic.gameState.ImmutableGameState;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class GameStateConverter implements NetworkInputConverter<ImmutableGameState> {

    public static final int SIZE = 2;
    private final BoardConverter boardConverter;

    public GameStateConverter(BoardConverter boardConverter) {
        this.boardConverter = boardConverter;
    }

    @Override
    public double[] convert(ImmutableGameState gameState) {
        double[] boardData = this.boardConverter.convert(gameState.getBoard());

        int teamOneScore = gameState.getScoreForTeam(Team.ONE).score();
        int teamTwoScore = gameState.getScoreForTeam(Team.TWO).score();
        int scoreSum = teamOneScore + teamTwoScore;
        double[] scoreData = new double[SIZE];
        if (scoreSum != 0) {
            scoreData[0] = teamOneScore / (double) scoreSum;
            scoreData[1] = teamTwoScore / (double) scoreSum;
        } else {
            scoreData[0] = 0;
            scoreData[1] = 0;
        }

        DoubleStream boardAndScoreStream = DoubleStream.concat(Arrays.stream(boardData), Arrays.stream(scoreData));
        return boardAndScoreStream.toArray();
    }

    @Override
    public int size() {
        return this.boardConverter.size() + SIZE;
    }
}
