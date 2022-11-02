package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
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

    final ImmutableMap<Coordinates, Team> penguinCoordinateTeamMap;

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

    public BoardPeek(long ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam, long ifFishFieldThenFishModulo2OtherwisePenguinCount, long nonZeroFishCount, ImmutableMap<Coordinates, Team> penguinCoordinateTeamMap) {
        this.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam;
        this.ifFishFieldThenFishModulo2OtherwisePenguinCount = ifFishFieldThenFishModulo2OtherwisePenguinCount;
        this.nonZeroFishCount = nonZeroFishCount;
        this.penguinCoordinateTeamMap = penguinCoordinateTeamMap;
    }

    public static BoardPeek fromStreams(Stream<Field> fields) { // assumes field.count() == 64
        long ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam = 0;
        long ifFishFieldThenFishModulo2OtherwisePenguinCount = 0;
        long nonZeroFishCount = 0;
        ImmutableMap.Builder<Coordinates, Team> penguinTeamMapBuilder =
                ImmutableMap.builder();
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
                    penguinTeamMapBuilder = penguinTeamMapBuilder.put(new Coordinates(x*2+y%2, y), penguinTeam);
                }
            }
        }
        return new BoardPeek(ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam,
                ifFishFieldThenFishModulo2OtherwisePenguinCount, nonZeroFishCount,
                penguinTeamMapBuilder.build());
    }

    public BoardPeek withMovePerformed(Move move, ITeam team) {
        Stream<Coordinates> coordStream = GameRuleLogic.createBoardCoordinateStream();
        return fromStreams(coordStream.map(coordinates -> {
            if (Objects.equals(coordinates, move.getFrom()))
                return new Field(0, null);
            if (Objects.equals(coordinates, move.getTo()))
                return new Field(0, (Team) team); // The field to be moved from and thus the penguin
            return get(coordinates);
        }));
    }

    public Collection<Pair<Coordinates, Team>> getPenguins() {
        return penguinCoordinateTeamMap.entrySet().stream().map(coordinatesTeamEntry ->
                new Pair<>(coordinatesTeamEntry.getKey(), coordinatesTeamEntry.getValue())).toList();
    }

    public Field get(Coordinates position) {
        int x = position.getX()/2, y = position.getY();
        boolean isFishField = getBitFrom8x8BitSet(x, y, nonZeroFishCount);
        if(isFishField) {
            long greaterThanTwoBitSet = this.ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam;
            boolean greaterThanTwo = getBitFrom8x8BitSet(x, y, greaterThanTwoBitSet);
            long oddBitSet = ifFishFieldThenFishModulo2OtherwisePenguinCount;
            boolean odd = getBitFrom8x8BitSet(x, y, oddBitSet);
            int fishCount = (greaterThanTwo ? 2 : 0) + (odd ? 1 : 0);
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

    @Override
    public String toString() {
        return "BoardPeek{" +
                "ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam=" + Long.toBinaryString(ifFishFieldThenFishCountHigherThanTwoOtherwisePenguinTeam) +
                ", ifFishFieldThenFishModulo2OtherwisePenguinCount=" + Long.toBinaryString(ifFishFieldThenFishModulo2OtherwisePenguinCount) +
                ", nonZeroFishCount=" + Long.toBinaryString(nonZeroFishCount) +
                '}';
    }
}
