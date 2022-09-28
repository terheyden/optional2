package com.terheyden.optional2;

/**
 * Predicate2 interface.
 */
@FunctionalInterface
public interface Predicate2<S, T> {

    boolean test(S s, T t);

    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another.
     * When evaluating the composed predicate, if this predicate is {@code false}, then the {@code other} predicate
     * is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of
     * this predicate throws an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of
     *         this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate2<S, T> and(Predicate2<? super S, ? super T> other) {
        return (s, t) -> test(s, t) && other.test(s, t);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default Predicate2<S, T> negate() {
        return (s, t) -> !test(s, t);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another.
     * When evaluating the composed predicate, if this predicate is {@code true}, then the {@code other} predicate
     * is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if evaluation of
     * this predicate throws an exception, the {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the
     * {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate2<S, T> or(Predicate2<? super S, ? super T> other) {
        return (s, t) -> test(s, t) || other.test(s, t);
    }
}
