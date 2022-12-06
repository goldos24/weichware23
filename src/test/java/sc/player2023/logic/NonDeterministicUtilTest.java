package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NonDeterministicUtilTest {

    @Test
    void shuffleStream() {
        List<Integer> testArray = List.of(1, 2, 3, 4);
        Stream<Integer> sourceStream = testArray.stream();
        Stream<Integer> shuffledStream = NonDeterministicUtil.shuffleStream(sourceStream);
        List<Integer> resultingArray =  shuffledStream.sorted().toList();
        assertEquals(testArray, resultingArray);
    }
}