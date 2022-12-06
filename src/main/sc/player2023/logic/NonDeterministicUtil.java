package sc.player2023.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class NonDeterministicUtil {
    public static <T> Stream<T> shuffleStream(Stream<T> source) {
        List<T> contents = new ArrayList<>(source.toList());
        Collections.shuffle(contents);
        return contents.stream();
    }
}
