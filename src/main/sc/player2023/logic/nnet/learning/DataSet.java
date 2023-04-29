package sc.player2023.logic.nnet.learning;

import java.util.Arrays;
import java.util.List;

public record DataSet(DataSetRow... rows) {

    public List<DataSetRow> rowsList() {
        return Arrays.stream(rows).toList();
    }
}
