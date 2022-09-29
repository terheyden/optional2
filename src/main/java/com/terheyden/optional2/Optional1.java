package com.terheyden.optional2;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedPredicate;
import io.vavr.CheckedRunnable;

/**
 * Optional1 class.
 */
public class Optional1<C> {

    /**
     * There's no generic type, for casting.
     */
    private static final Optional1 EMPTY = new Optional1<>(Optional.empty());

    private final Optional<C> obj1;

    /**
     * Create via {@link Optional2#ofNullable(Object)}.
     */
    /* package */ Optional1(@Nullable C obj1) {
        this.obj1 = Optional.ofNullable(obj1);
    }

    /**
     * Create via {@link Optional2#ofNullable(Object)}.
     */
    /* package */ Optional1(Optional<C> obj1) {
        this.obj1 = obj1;
    }

    /**
     * Returns an empty immutable singleton {@code Optional2} instance.
     */
    @SuppressWarnings("unchecked")
    public static <C> Optional1<C> empty() {
        return (Optional1<C>) EMPTY;
    }

    /**
     * Adds a second Optional value to track via {@link Optional2}.
     */
    public <D> Optional2<C, D> andOfNullable(@Nullable D obj2) {
        return isEmpty()
            ? Optional2.empty()
            : new Optional2<>(obj1, Optional.ofNullable(obj2));
    }

    /**
     * Adds a second Optional value to track via {@link Optional2}.
     */
    public <D> Optional2<C, D> andOfNullable(CheckedFunction1<C, D> valueMapper) {

        // Don't calculate anything if we're empty.
        if (isEmpty()) {
            return Optional2.empty();
        }

        // Calculate the second value.
        D obj2 = valueMapper.unchecked().apply(obj1.get());
        return new Optional2<>(obj1, Optional.ofNullable(obj2));
    }

    /**
     * Adds a second Optional value to track via {@link Optional2}.
     */
    public <D> Optional2<C, D> andOf(D obj2) {
        return isEmpty()
            ? Optional2.empty()
            : new Optional2<>(obj1, Optional.of(obj2));
    }

    /**
     * Adds a second Optional value to track via {@link Optional2}.
     */
    public <D> Optional2<C, D> andOf(CheckedFunction1<C, D> valueMapper) {

        // Don't calculate anything if we're empty.
        if (isEmpty()) {
            return Optional2.empty();
        }

        // Calculate the second value.
        D obj2 = valueMapper.unchecked().apply(obj1.get());
        return new Optional2<>(obj1, Optional.of(obj2));
    }

    /**
     * A chainable version of {@link #orElseThrow(CheckedFunction0)}.
     * Throws the given exception if either value is null.
     *
     * @return this, for chaining
     * @throws NoSuchElementException if either value is null
     */
    public Optional1<C> throwIfEmpty(CheckedFunction0<? extends RuntimeException> exceptionSupplier) {
        obj1.orElseThrow(exceptionSupplier.unchecked());
        return this;
    }

    /**
     * A chainable version of {@link #orElseThrow(CheckedFunction0)}.
     * Throws the given exception if either value is null.
     *
     * @return this, for chaining
     * @throws NoSuchElementException if either value is null
     */
    public Optional1<C> throwIfEmpty() {
        obj1.orElseThrow();
        return this;
    }

    /**
     * Run the given {@link CheckedRunnable} if the value is null.
     *
     * @return this, for chaining
     */
    public Optional1<C> runIfEmpty(CheckedRunnable runIfEmpty) {

        if (isEmpty()) {
            runIfEmpty.unchecked().run();
        }

        return this;
    }

    /**
     * Similar to {@link Optional#get()}, returns the value, if present,
     * otherwise throws {@link NoSuchElementException}.
     */
    public C get() {
        return obj1.get();
    }

    /**
     * Convert this single-value {@link Optional2} into a Java {@link Optional}.
     */
    public Optional<C> getOptional() {
        return obj1;
    }

    /**
     * If this value is null, use the supplier to get a non-empty value to use instead.
     * The supplier returns an {@link Optional} and not a {@link Optional2}
     * because it's likely a more common use case, and {@code Optional2} can be converted
     * to an {@link Optional}.
     *
     * @see Optional#or(Supplier)
     */
    public Optional1<C> or(CheckedFunction0<Optional<C>> supplyIfEmpty) {

        return isEmpty()
            ? new Optional1<>(supplyIfEmpty.unchecked().apply())
            : this;
    }

    /**
     * If this value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional1<C> or(Optional1<C> useIfEmpty) {

        return isEmpty()
            ? useIfEmpty
            : this;
    }

    /**
     * If this value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional1<C> or(Optional<C> useIfEmpty) {

        return isEmpty()
            ? new Optional1<>(useIfEmpty)
            : this;
    }

    /**
     * If this value is null, use the given replacement value instead.
     *
     * @see Optional#or(Supplier)
     */
    public Optional1<C> or(C useIfEmpty) {

        return isEmpty()
            ? new Optional1<>(useIfEmpty)
            : this;
    }

    /**
     * Returns the value, if present, otherwise returns the given value.
     */
    public C orElse(C other) {
        return obj1.orElse(other);
    }

    /**
     * Returns the value if present, else null.
     * Equivalent to: {@code orElse(null)}, but with proper {@code @Nullable} annotations.
     */
    @Nullable
    public C orElseNull() {
        return obj1.orElse(null);
    }

    /**
     * Returns the value, if present, otherwise computes the value from the given supplier.
     */
    public C orElseGet(CheckedFunction0<? extends C> other) {
        return obj1.orElseGet(other.unchecked());
    }

    /**
     * Returns the value, if present, otherwise throws the exception from the given supplier.
     */
    public <X extends Throwable> C orElseThrow(CheckedFunction0<? extends X> exceptionSupplier) throws X {
        return obj1.orElseThrow(exceptionSupplier.unchecked());
    }

    /**
     * Returns the value, if present, otherwise throws a {@link NoSuchElementException}.
     *
     * @throws NoSuchElementException if the value is not present
     */
    public C orElseThrow() {
        return obj1.orElseThrow();
    }

    public boolean isPresent() {
        return obj1.isPresent();
    }

    public boolean isEmpty() {
        return obj1.isEmpty();
    }

    /**
     * Consume the value, if present.
     *
     * @return this, for chaining
     * @see Optional#ifPresent(Consumer)
     */
    public Optional1<C> ifPresent(CheckedConsumer<? super C> consumer) {
        obj1.ifPresent(consumer.unchecked());
        return this;
    }

    /**
     * Consume the value with the given consumer,
     * or run the given runnable if the value is null.
     *
     * @return this, for chaining
     * @see Optional#ifPresentOrElse(Consumer, Runnable)
     */
    public Optional1<C> ifPresentOrElse(CheckedConsumer<? super C> consumer, CheckedRunnable emptyAction) {
        obj1.ifPresentOrElse(consumer.unchecked(), emptyAction.unchecked());
        return this;
    }

    /**
     * Run the given {@link CheckedRunnable} if the value is null.
     *
     * @return this, for chaining
     * @see Optional#ifPresent(Consumer)
     */
    public Optional1<C> ifEmpty(CheckedRunnable runIfEmpty) {

        if (isEmpty()) {
            runIfEmpty.unchecked().run();
        }

        return this;
    }

    public Optional1<C> filter(CheckedPredicate<? super C> predicate) {
        return isEmpty()
            ? this
            : new Optional1<>(obj1.filter(predicate.unchecked()));
    }

    public <B> Optional1<B> map(CheckedFunction1<? super C, ? extends B> mapper) {
        return isEmpty()
            ? empty()
            : new Optional1<>(obj1.map(mapper.unchecked()));
    }

    /**
     * FlatMap an {@link Optional} value from the given mapper.
     * We FlatMap from an {@code Optional} and not another {@code Optional2}
     * because {@code Optional}s are much more common, and {@code Optional2}s can
     * always be reduced to {@code Optional}s.
     *
     * @see Optional#flatMap(Function)
     */
    public <B> Optional1<B> flatMap(CheckedFunction1<? super C, ? extends Optional<? extends B>> mapper) {
        return isEmpty()
            ? empty()
            : new Optional1<>(obj1.flatMap(mapper.unchecked()));
    }

    @Override
    public String toString() {
        return "Optional2[%s]".formatted(obj1.isEmpty() ? "null" : obj1.get());
    }
}
