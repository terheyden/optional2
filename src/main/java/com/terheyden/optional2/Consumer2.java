package com.terheyden.optional2;

/**
 * Consumer2 class.
 */
@FunctionalInterface
public interface Consumer2<S, T> {

    void accept(S s, T t);

    /**
     * Returns a composed {@code Consumer2} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer2} that performs in sequence this operation
     *         followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer2<S, T> andThen(Consumer2<? super S, ? super T> after) {
        return (s, t) -> {
            accept(s, t);
            after.accept(s, t);
        };
    }

    default CheckedConsumer2<S, T> checked() {
        return (s, t) -> accept(s, t);
    }
}
