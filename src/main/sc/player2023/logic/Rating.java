package sc.player2023.logic;

import java.util.Objects;

/**
 * @author Till Fransson
 * @since 14.10.2022
 */
public record Rating(double rating) {

    public static final Rating ZERO = new Rating(0.0);

    public static final Rating NEGATIVE_INFINITY = new Rating(Double.NEGATIVE_INFINITY);

    public static final Rating POSITIVE_INFINITY = new Rating(Double.POSITIVE_INFINITY);
    public static final Rating ONE = new Rating(1.0);
    public static final Rating TWO = new Rating(2.0);
    public static final Rating FIVE = new Rating(5.0);

    public Rating add(Rating other) {
        return this.add(other.rating);
    }

    public Rating add(double rating) {
        double sum = this.rating + rating;
        return new Rating(sum);
    }

    public Rating subtract(Rating other) {
        return this.subtract(other.rating);
    }

    public Rating subtract(double rating) {
        double difference = this.rating - rating;
        return new Rating(difference);
    }

    public Rating multiply(double rating) {
        double product = this.rating * rating;
        return new Rating(product);
    }

    public Rating negate() {
        return new Rating(-rating);
    }

    public boolean isGreaterThan(Rating other) {
        return rating > other.rating;
    }

    public boolean isLessThan(Rating other) {
        return rating < other.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating other)) {
            return false;
        }
        return Double.compare(rating, other.rating) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating);
    }

    @Override
    public String toString() {
        return "Rating {" + rating + "}";
    }
}
