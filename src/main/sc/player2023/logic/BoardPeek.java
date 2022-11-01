package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.Field;

import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.stream.Stream;

@Immutable
public class BoardPeek {
    /*
     * TODO:
     *  complete reimplementation of board mapping it to integers
     *  rename to ImmutableBoard
     *  have similar methods to sc.plugin2023.Board, just immutable
     *  replace every usage of board in ratings et al. with BoardPeek / ImmutableBoard
     */

    private final Board board;

    public BoardPeek(Board board) {
        this.board = board;
    }


    public static BoardPeek fromStreams(Stream<Field> fields) { // assumes field.count() == 64
        List<List<Field>> fieldsForBoard = new ArrayList<>(GameRuleLogic.BOARD_HEIGHT);
        Iterator<Field> fieldIterator = fields.iterator();
        for (int y = 0; y < GameRuleLogic.BOARD_HEIGHT; ++y) {
            List<Field> fieldRow = new ArrayList<>(GameRuleLogic.BOARD_WIDTH);
            for(int x = 0; x < GameRuleLogic.BOARD_WIDTH; ++x) {
                fieldRow.add(fieldIterator.next());
            }
            fieldsForBoard.add(fieldRow);
        }
        return new BoardPeek(new Board(fieldsForBoard));
    }

    public Collection<Pair<Coordinates, Team>> getPenguins() {
        return board.getPenguins();
    }

    public Field get(Coordinates position) {
        return board.get(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoardPeek boardPeek = (BoardPeek) o;
        return Objects.equals(board, boardPeek.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }

    @Override
    public String toString() {
        return "BoardPeek {" + board + "}";
    }
}
