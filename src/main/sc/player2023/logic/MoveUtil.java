package sc.player2023.logic;

import com.google.common.collect.ImmutableList;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;

/**
 * @author Till Fransson
 * @since 04.11.2022
 */
public class MoveUtil {

    private MoveUtil() {
    }

    public static List<Move> sortPossibleMoves(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater) {
        Iterable<Move> possibleMoves = new PossibleMoveIterable(gameState);

        List<RatedMove> ratedMoves = new ArrayList<>();

        for (Move possibleMove : possibleMoves) {
            ImmutableGameState afterMove = withMovePerformed(gameState, possibleMove);
            Rating rating = rater.rate(afterMove);
            RatedMove ratedMove = new RatedMove(rating, possibleMove);
            ratedMoves.add(ratedMove);
        }

        ratedMoves.sort(RatedMove::compareTo);

        ImmutableList.Builder<Move> builder = new ImmutableList.Builder<>();

        for (RatedMove ratedMove : ratedMoves) {
            builder.add(ratedMove.move());
        }
        return builder.build();
    }
}
