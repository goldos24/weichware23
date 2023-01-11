package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;

public record TransPositionTableEntry(@Nonnull Rating rating,
                                      @Nonnull RatingType ratingType
                                      ) {

}
