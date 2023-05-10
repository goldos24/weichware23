package sc.player2023.logic.nnet.data;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Field;

public class BoardConverter implements NetworkInputConverter<BoardPeek> {

    public static final int SIZE = GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT * 2;

    @Override
    public double[] convert(BoardPeek board) {
        double[] data = new double[SIZE];

        for (int j = 0; j < GameRuleLogic.BOARD_HEIGHT; ++j) {
            for (int i = 0; i < GameRuleLogic.BOARD_WIDTH; ++i) {
                int index = j * GameRuleLogic.BOARD_WIDTH + i;
                Field field = board.get(new Coordinates(i * 2 + j % 2, j));
                data[index * 2] = fishToValue(field.getFish());
                data[index * 2 + 1] = penguinToValue(field.getPenguin());
            }
        }

        return data;
    }

    private static final int MAX_FISH = 4;

    private static double fishToValue(int fish) {
        return fish / (double) MAX_FISH;
    }

    private static double penguinToValue(Team penguin) {
        return penguin == Team.ONE ? 0.0 :
               penguin == Team.TWO ? 1.0 :
               0.5;
    }

    @Override
    public int size() {
        return SIZE;
    }
}
