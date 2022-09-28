package com.terheyden.optional2;

/**
 * Optional2Utils interface.
 */
/* package */ final class Optional2Utils {

    private Optional2Utils() {
        // Private constructor since this shouldn't be instantiated.
    }

    /**
     * Throw any exception unchecked.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable, R> R throwUnchecked(Throwable throwable) throws E {
        throw (E) throwable;
    }
}
