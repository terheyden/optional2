package com.terheyden.optional2;

/**
 * CheckedPredicate2 interface.
 */
@FunctionalInterface
public interface CheckedPredicate2<S, T> {

    boolean test(S pred1, T pred2) throws Throwable;

    default CheckedPredicate2<S, T> negate() {
        return (pred1, pred2) -> !test(pred1, pred2);
    }

    default Predicate2<S, T> unchecked() {
        return (pred1, pred2) -> {
            try {
                return test(pred1, pred2);
            } catch (Throwable e) {
                return Optional2Utils.throwUnchecked(e);
            }
        };
    }
}
