package sc.player2023.logic.board;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

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

    public BoardPeek(Board board) {
        BoardPeek initialBoard = BoardPeek.fromStreams(GameRuleLogic.createBoardCoordinateStream().map(
                board::get
        ));
        this.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = initialBoard.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam;
        this.ifFishFieldThenFishModulo2OtherwisePenguinCount = initialBoard.ifFishFieldThenFishModulo2OtherwisePenguinCount;
        this.nonZeroFishCount = initialBoard.nonZeroFishCount;
        this.penguinCoordinateTeamMap = initialBoard.penguinCoordinateTeamMap;
    }


    // multi-purpose variables for space efficiency
    final long
            ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
            ifFishFieldThenFishModulo2OtherwisePenguinCount,
            nonZeroFishCount;

    final PenguinCollection penguinCoordinateTeamMap;

    static long getBitSetPositionMask(int x, int y) {
        return 1L << (y * 8 + x);
    }

    static boolean getBitFrom8x8BitSet(int x, int y, long bitset) {
        return (bitset & getBitSetPositionMask(x, y)) != 0;
    }

    static long bitSet8x8WithSingleBitChanged(int x, int y, long bitset, boolean newBitValue) {
        if (newBitValue) {
            return bitset | getBitSetPositionMask(x, y);
        }
        return bitset & ~getBitSetPositionMask(x, y);
    }

    private static boolean getFishCountHigherThanTwoOrPenguinTeamForField(Field field) {
        if (field.getFish() > 2)
            return true;
        return field.getPenguin() == Team.TWO; // in case of no penguin it's null so false
    }

    private static boolean getFishModulo2Equals1OrIsPenguin(Field field) {
        if (field.getPenguin() != null) {
            return true; // isPenguin == true
        }
        return field.getFish() % 2 == 1;
    }

    public BoardPeek(long ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam, long ifFishFieldThenFishModulo2OtherwisePenguinCount, long nonZeroFishCount, PenguinCollection penguinCoordinateTeamMap) {
        this.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam;
        this.ifFishFieldThenFishModulo2OtherwisePenguinCount = ifFishFieldThenFishModulo2OtherwisePenguinCount;
        this.nonZeroFishCount = nonZeroFishCount;
        this.penguinCoordinateTeamMap = penguinCoordinateTeamMap;
    }

    public static BoardPeek fromStreams(Stream<Field> fields) { // assumes field.count() == 64
        long ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = 0;
        long ifFishFieldThenFishModulo2OtherwisePenguinCount = 0;
        long nonZeroFishCount = 0;
        List<Pair<Coordinates, Team>> penguinTeamMapBuilder = new ArrayList<>();
        Iterator<Field> fieldIterator = fields.iterator();
        for (int y = 0; y < GameRuleLogic.BOARD_HEIGHT; ++y) {
            for (int x = 0; x < GameRuleLogic.BOARD_WIDTH; ++x) {
                Field field = fieldIterator.next();
                ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = bitSet8x8WithSingleBitChanged(
                        x, y, ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
                        getFishCountHigherThanTwoOrPenguinTeamForField(field)
                );
                ifFishFieldThenFishModulo2OtherwisePenguinCount = bitSet8x8WithSingleBitChanged(
                        x, y, ifFishFieldThenFishModulo2OtherwisePenguinCount,
                        getFishModulo2Equals1OrIsPenguin(field)
                );
                nonZeroFishCount = bitSet8x8WithSingleBitChanged(x, y, nonZeroFishCount,
                        field.getFish() != 0);
                Team penguinTeam = field.getPenguin();
                if(penguinTeam != null) {
                    penguinTeamMapBuilder.add(new Pair<>(new Coordinates(x*2+y%2, y), penguinTeam));
                }
            }
        }
        return new BoardPeek(ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
                ifFishFieldThenFishModulo2OtherwisePenguinCount, nonZeroFishCount,
                PenguinCollection.fromOtherPenguinCollection(penguinTeamMapBuilder));
    }

    public BoardPeek withMovePerformed(Move move, ITeam team) {
        int toX = move.getTo().getX()/2, toY = move.getTo().getY();
        long newIfFishFieldThenFishModulo2OtherwisePenguinCount = bitSet8x8WithSingleBitChanged(toX, toY,
                ifFishFieldThenFishModulo2OtherwisePenguinCount, true); // penguin at target
        // Penguin team at target:
        long newIfFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = bitSet8x8WithSingleBitChanged(toX, toY,
                ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam, team == Team.TWO);
        long newNonZeroFishCountBitSet = bitSet8x8WithSingleBitChanged(
                toX, toY,
                nonZeroFishCount, false
        ); // penguin at target => no fish
        if(move.getFrom() == null) {
            return new BoardPeek(newIfFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
                    newIfFishFieldThenFishModulo2OtherwisePenguinCount,
                    newNonZeroFishCountBitSet,
                    penguinCoordinateTeamMap.withExtraPenguin(new Pair<>(move.getTo(), (Team) team)));
        }
        int fromX = move.getFrom().getX()/2, fromY = move.getFrom().getY();
        newIfFishFieldThenFishModulo2OtherwisePenguinCount = bitSet8x8WithSingleBitChanged(fromX, fromY,
                newIfFishFieldThenFishModulo2OtherwisePenguinCount, false); // no penguin move from pos
        newIfFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = bitSet8x8WithSingleBitChanged(
                fromX, fromY,
                newIfFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam, false); // nothing there = 0
        return new BoardPeek(newIfFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
                newIfFishFieldThenFishModulo2OtherwisePenguinCount,
                newNonZeroFishCountBitSet,
                penguinCoordinateTeamMap.withPenguinMoved(new Pair<>(move.getFrom(), (Team)team), move.getTo()));
    }

    public Collection<Pair<Coordinates, Team>> getPenguins() {
        return penguinCoordinateTeamMap;
    }

    public Field get(Coordinates position) {
        int x = position.getX()/2, y = position.getY();
        boolean isFishField = getBitFrom8x8BitSet(x, y, nonZeroFishCount);
        if(isFishField) {
            long greaterThanTwoBitSet = this.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam;
            boolean greaterThanTwo = getBitFrom8x8BitSet(x, y, greaterThanTwoBitSet);
            long oddBitSet = ifFishFieldThenFishModulo2OtherwisePenguinCount;
            boolean odd = getBitFrom8x8BitSet(x, y, oddBitSet);
            int fishCount = (greaterThanTwo ? 3 : 1) + (odd ? 0 : 1);
            return new Field(fishCount, null);
        }
        boolean isPenguin = getBitFrom8x8BitSet(x, y, ifFishFieldThenFishModulo2OtherwisePenguinCount);
        if (isPenguin) {
            boolean penguinInTeamTwo = getBitFrom8x8BitSet(x, y, ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam);
            return new Field(0, penguinInTeamTwo ? Team.TWO : Team.ONE);
        }
        return new Field(0, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPeek boardPeek = (BoardPeek) o;
        return ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam == boardPeek.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam && ifFishFieldThenFishModulo2OtherwisePenguinCount == boardPeek.ifFishFieldThenFishModulo2OtherwisePenguinCount && nonZeroFishCount == boardPeek.nonZeroFishCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam, ifFishFieldThenFishModulo2OtherwisePenguinCount, nonZeroFishCount);
    }

    Map<String, String> toStringEncodingMap = Map.of("0" ," ",
            "R", "\u001B[32mR\u001B[0m",
            "B", "\u001B[35mB\u001B[0m",
            "1", "-",
            "2", "=");

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("BoardPeek{\n");
        for (int j = 0; j < GameRuleLogic.BOARD_HEIGHT; ++j) {
            if(j%2==1)
                result.append(" ");
            for(int i = 0; i < GameRuleLogic.BOARD_WIDTH; ++i) {
                Field field = get(new Coordinates(i * 2 + j % 2, j));
                result.append(toStringEncodingMap.get(field.toString()) != null ? toStringEncodingMap.get(field.toString()) : field);
                result.append(" ");
            }
            result.append("\n");
        }
        result.append("}");
        return result.toString();
    }
}
