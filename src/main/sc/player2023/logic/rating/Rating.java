package sc.player2023.logic.rating;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 14.10.2022
 */
public record Rating(int rating) implements Comparable<Rating> {

    public static final Rating ZERO = new Rating(0);

    public static final int PRIMITIVE_UPPER_BOUND = 1_000_000; // lower than Integer.MAX_VALUE to prevent overflows
    public static final int PRIMITIVE_LOWER_BOUND = -PRIMITIVE_UPPER_BOUND;

    public static final Rating NEGATIVE_INFINITY = new Rating(PRIMITIVE_LOWER_BOUND);

    public static final Rating POSITIVE_INFINITY = new Rating(PRIMITIVE_UPPER_BOUND);
    public static final Rating ONE = new Rating(1);
    public static final Rating TWO = new Rating(2);
    public static final Rating FIVE = new Rating(5);

    public Rating add(Rating other) {
        return this.add(other.rating);
    }

    public Rating add(int rating) {
        int sum = this.rating + rating;
        return new Rating(sum);
    }

    public Rating subtract(Rating other) {
        return this.subtract(other.rating);
    }

    public Rating subtract(int rating) {
        int difference = this.rating - rating;
        return new Rating(difference);
    }

    public Rating multiply(int rating) {
        int product = this.rating * rating;
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
    public int compareTo(@Nonnull Rating o) {
        return Integer.compare(rating, o.rating);
    }
}
