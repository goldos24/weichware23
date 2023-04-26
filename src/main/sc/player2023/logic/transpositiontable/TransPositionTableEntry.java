package sc.player2023.logic.transpositiontable;

import sc.player2023.logic.rating.Rating;

import java.util.Objects;

public final class TransPositionTableEntry {
    private final Rating rating;
    private final RatingType ratingType;

    public TransPositionTableEntry(Rating rating,
                                   RatingType ratingType
    ) {
        this.rating = rating;
        this.ratingType = ratingType;
    }

    public Rating rating() {
        return rating;
    }

    public RatingType ratingType() {
        return ratingType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TransPositionTableEntry) obj;
        return Objects.equals(this.rating, that.rating) &&
                Objects.equals(this.ratingType, that.ratingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, ratingType);
    }

    @Override
    public String toString() {
        return "TransPositionTableEntry[" +
                "rating=" + rating + ", " +
                "ratingType=" + ratingType + ']';
    }


}
