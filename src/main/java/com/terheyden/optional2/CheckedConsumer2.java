package com.terheyden.optional2;

/**
 * Functional interface for accepting two values.
 */
@FunctionalInterface
public interface CheckedConsumer2<S, T> {

    /**
     * Accept two values and perform some action.
     */
    void accept(S s, T t) throws Throwable;

    /**
     * Returns a composed {@code CheckedConsumer2} that performs, in sequence, this operation followed by
     * the {@code after} operation. If performing either operation throws an exception, it is relayed to the caller
     * of the composed operation. If performing this operation throws an exception, the {@code after} operation
     * will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code CheckedConsumer2} that performs in sequence this operation
     *         followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default CheckedConsumer2<S, T> andThen(CheckedConsumer2<? super S, ? super T> after) {
        return (s, t) -> {
            accept(s, t);
            after.accept(s, t);
        };
    }

    default Consumer2<S, T> unchecked() {
        return (s, t) -> {
            try {
                accept(s, t);
            } catch (Throwable throwable) {
                Optional2Utils.throwUnchecked(throwable);
            }
        };
    }
}
