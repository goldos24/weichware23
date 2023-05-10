package sc.player2023.logic.nnet.data;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Field;

public class OneHotBoardConverter implements NetworkInputConverter<BoardPeek> {

    public static final int POSSIBLE_PENGUIN_STATES = 3;
    public static final int SIZE = GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT * (1 + POSSIBLE_PENGUIN_STATES);

    @Override
    public double[] convert(BoardPeek board) {
        double[] data = new double[SIZE];

        for (int j = 0; j < GameRuleLogic.BOARD_HEIGHT; ++j) {
            for (int i = 0; i < GameRuleLogic.BOARD_WIDTH; ++i) {
                int index = j * GameRuleLogic.BOARD_WIDTH + i;
                Field field = board.get(new Coordinates(i * 2 + j % 2, j));
                data[index * 2] = fishToValue(field.getFish());
                writePenguinValue(field.getPenguin(), data, index);
            }
        }
        return data;
    }

    private static final int MAX_FISH = 4;

    private static double fishToValue(int fish) {
        return fish / (double) MAX_FISH;
    }

    private static void writePenguinValue(Team penguin, double[] data, int index) {
        if (penguin == Team.ONE) {
            data[index * 2 + 1] = 1.0;
            data[index * 2 + 2] = 0.0;
            data[index * 2 + 3] = 0.0;
        } else if (penguin == Team.TWO) {
            data[index * 2 + 1] = 0.0;
            data[index * 2 + 2] = 1.0;
            data[index * 2 + 3] = 0.0;
        } else {
            data[index * 2 + 1] = 0.0;
            data[index * 2 + 2] = 0.0;
            data[index * 2 + 3] = 1.0;
        }
    }

    @Override
    public int size() {
        return SIZE;
    }
}
